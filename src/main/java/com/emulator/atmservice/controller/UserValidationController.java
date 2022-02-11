package com.emulator.atmservice.controller;

import com.emulator.atmservice.controller.request.AuthenticationRequest;
import com.emulator.atmservice.controller.request.CardDto;
import com.emulator.atmservice.controller.response.AuthorizationResponse;
import com.emulator.atmservice.controller.response.SessionValidityResponse;
import com.emulator.atmservice.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class UserValidationController {
    private final UserValidationService userValidationService;


    @PostMapping("/validate")
    public ResponseEntity<SessionValidityResponse> cardAuthentication(@RequestBody @Valid CardDto cardDto) {
        return ResponseEntity.ok(userValidationService.validate(cardDto));
    }

    @PostMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> cardAuthorization(@RequestHeader("Authorization") String sessionKey,
                                                                   @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userValidationService.authorization(sessionKey, request));
    }
}
