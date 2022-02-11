package com.emulator.atmservice.service.impl;

import com.emulator.atmservice.controller.response.AuthorizationResponse;
import com.emulator.atmservice.exception.InvalidAuthorizationTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationTokenDb {
    @Value("${session.token.duration.milis}")
    private Long duration;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();


    public AuthorizationResponse generateToken(String accountNumber) {
        String token = UUID.randomUUID().toString();
        map.put(token, accountNumber);

        scheduleTokenExpiration(token);

        return AuthorizationResponse.builder()
                .duration(duration)
                .token(token)
                .build();
    }

    public void verifyGivenToken(String token){
        if (map.get(token) == null) {
            throw new InvalidAuthorizationTokenException("Invalid session token");
        }
    }

    public String getAccountNumberByToken(String token){
        ConcurrentHashMap<String, String> copy = new ConcurrentHashMap<>(map);
        return copy.get(token);
    }

    private void scheduleTokenExpiration(String token) {
        log.info("Expiring session key has been scheduled");
        threadPoolTaskScheduler.schedule(() -> {
                    map.remove(token);
                    log.info("Key has been removed from SessionTokenDb {}", map.get(token));

                },
                Instant.now().plus(duration, ChronoUnit.MILLIS));
    }
}
