package com.emulator.atmservice.service.impl;

import com.emulator.atmservice.client.BankServiceClient;
import com.emulator.atmservice.controller.request.AuthenticationRequest;
import com.emulator.atmservice.controller.request.CardDto;
import com.emulator.atmservice.controller.response.AuthorizationResponse;
import com.emulator.atmservice.controller.response.SessionValidityResponse;
import com.emulator.atmservice.db.ValidatedCard;
import com.emulator.atmservice.exception.CardNotValidException;
import com.emulator.atmservice.exception.InvalidCredentialsException;
import com.emulator.atmservice.exception.CardSessionValidationDb;
import com.emulator.atmservice.service.CardValidationService;
import com.emulator.atmservice.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final BankServiceClient bankServiceClient;
    private final CardValidationService cardValidationService;
    private final CardSessionValidationDb cardSessionValidationDb;
    private final AuthorizationTokenDb authorizationTokenDb;

    @Override
    public SessionValidityResponse validate(CardDto cardDto) {
        String accountNumber = checkIfUserExistsAndValidateCardNumber(cardDto);
        return cardSessionValidationDb.generateSession(cardDto,accountNumber);
    }

    @Override
    public AuthorizationResponse authorization(String sessionKey, AuthenticationRequest request) {
        ValidatedCard validatedCardBySessionKey = cardSessionValidationDb.getValidatedCardBySessionKey(sessionKey);
        authenticateUser(request, validatedCardBySessionKey, sessionKey);
        return authorizationTokenDb.generateToken(validatedCardBySessionKey.getAccountNumber());
    }

    private String checkIfUserExistsAndValidateCardNumber(CardDto cardDto) {
        String cardNumber = cardDto.getCardNumber();
        if (!cardValidationService.validateCard(cardNumber)) {
            throw new CardNotValidException(
                    "Given card with number " + cardNumber + " doesn't match the pattern");
        }
        return bankServiceClient.doesUserExist(cardNumber);
    }

    private void authenticateUser(AuthenticationRequest request, ValidatedCard validatedCardBySessionKey, String sessionKey) {
        boolean authenticate = bankServiceClient.authenticate(
                request.getCredential(),
                request.getAuthMethod(),
                validatedCardBySessionKey.getAccountNumber());
        if (!authenticate) {
            cardSessionValidationDb.incrementNumberOfTries(sessionKey);
            throw new InvalidCredentialsException("Invalid auth credentials were present");
        }
    }
}
