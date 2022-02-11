package com.emulator.atmservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SessionValidityResponse {
    private String sessionKey;
    private Long validityTime;
}
