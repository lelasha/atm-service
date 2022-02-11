package com.emulator.atmservice.exception;

import com.emulator.atmservice.controller.request.CardDto;
import com.emulator.atmservice.controller.response.SessionValidityResponse;
import com.emulator.atmservice.db.CardHasBeenBlockedException;
import com.emulator.atmservice.db.ValidatedCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardSessionValidationDb {

    @Value("${card.validation.session.duration.milis}")
    private Long duration;
    @Value("${card.validation.blockTimeRange.seconds}")
    private Long cardBlockingTimeRangeInSeconds;
    @Value("${card.validation.numberOfTries}")
    private Integer numberOfTries;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ConcurrentHashMap<String, ValidatedCard> validatedCardMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Instant> blockedCardMap = new ConcurrentHashMap<>();


    public SessionValidityResponse generateSession(CardDto cardDto, String accountNumber) {
        String sessionKey = UUID.randomUUID().toString();
        ValidatedCard card = ValidatedCard.builder()
                .cardNumber(cardDto.getCardNumber())
                .accountNumber(accountNumber)
                .cuttingPointForNumberOfTries(Instant.now())
                .build();
        validatedCardMap.put(sessionKey, card);

        scheduleToRemoveKeyFromDb(sessionKey);

        return SessionValidityResponse.builder()
                .sessionKey(sessionKey)
                .validityTime(duration)
                .build();
    }

    public ValidatedCard getValidatedCardBySessionKey(String sessionKey) {
        Map<String, ValidatedCard> copy = new ConcurrentHashMap<>(validatedCardMap);
        ValidatedCard validatedCard = copy.get(sessionKey);
        if (validatedCard == null) {
            throw new InvalidCardSessionException("Invalid session key");
        }
        if (blockedCardMap.get(validatedCard.getCardNumber()) != null) {
            throw new CardHasBeenBlockedException("Card is blocked, You are out of number of tries " + numberOfTries);
        }

        return validatedCard;
    }

    private void scheduleToRemoveKeyFromDb(String sessionKey) {
        log.info("Expiring card validation key has been scheduled");
        threadPoolTaskScheduler.schedule(() -> {
                    validatedCardMap.remove(sessionKey);
                    log.info("Key has been removed from card validation db {}", validatedCardMap.get(sessionKey));
                },
                Instant.now().plus(duration, ChronoUnit.MILLIS));
    }

    public void incrementNumberOfTries(String sessionKey) {
        int counter = 1;
        ValidatedCard validatedCard = validatedCardMap.get(sessionKey);
        Instant generatedTokenTimePlusBlockDuration =
                validatedCard.getCuttingPointForNumberOfTries()
                        .plus(cardBlockingTimeRangeInSeconds, ChronoUnit.SECONDS);

        checkIfBlockedDurationHasPassed(counter, validatedCard, generatedTokenTimePlusBlockDuration);
    }

    private void checkIfBlockedDurationHasPassed(int counter,
                                                 ValidatedCard validatedCard,
                                                 Instant generatedTokenTimePlusBlockDuration) {
        if (Instant.now().isBefore(generatedTokenTimePlusBlockDuration)) {
            counter = validatedCard.getAttemptedTriesCounter().incrementAndGet();
            log.info("Invalid credentials were tried {} times", counter);
            if (counter == numberOfTries) {
                blockedCardMap.put(validatedCard.getCardNumber(), Instant.now());
            }
        } else {
            validatedCard.getAttemptedTriesCounter().set(counter);
            validatedCard.setCuttingPointForNumberOfTries(Instant.now());
        }
    }
}
