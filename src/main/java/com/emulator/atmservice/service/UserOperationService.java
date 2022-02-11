package com.emulator.atmservice.service;

import com.emulator.atmservice.controller.response.UserTransactionDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface UserOperationService {
    BigDecimal deposit(BigDecimal amount, String token);

    BigDecimal withdraw(BigDecimal amount, String token);

    BigDecimal balance(String token);

    List<UserTransactionDto> print(String token, Instant startDate, Instant endDate);
}
