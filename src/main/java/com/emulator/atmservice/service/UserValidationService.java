package com.emulator.atmservice.service;

import com.emulator.atmservice.controller.request.AuthenticationRequest;
import com.emulator.atmservice.controller.request.CardDto;
import com.emulator.atmservice.controller.response.AuthorizationResponse;
import com.emulator.atmservice.controller.response.SessionValidityResponse;

public interface UserValidationService {
    AuthorizationResponse authorization(String sessionKey, AuthenticationRequest credential);

    SessionValidityResponse validate(CardDto cardDto);
}
