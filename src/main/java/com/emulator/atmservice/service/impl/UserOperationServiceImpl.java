package com.emulator.atmservice.service.impl;

import com.emulator.atmservice.client.BankServiceClient;
import com.emulator.atmservice.controller.response.UserTransactionDto;
import com.emulator.atmservice.service.UserOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOperationServiceImpl implements UserOperationService {

    private final BankServiceClient bankServiceClient;
    private final AuthorizationTokenDb authorizationTokenDb;

    @Override
    public BigDecimal deposit(BigDecimal amount, String token) {
        String accountNumber = authorizationTokenDb.getAccountNumberByToken(token);
        log.info("Sending deposit call to bank-service client");
        BigDecimal deposit = bankServiceClient.deposit(amount, accountNumber);
        log.info("Deposit call has been completed Successfully");
        return deposit;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amount, String token) {
        String accountNumber = authorizationTokenDb.getAccountNumberByToken(token);
        log.info("Sending withdraw call to bank-service client");
        BigDecimal withdraw = bankServiceClient.withdraw(amount, accountNumber);
        log.info("Withdraw call has been completed Successfully");
        return withdraw;
    }

    @Override
    public BigDecimal balance(String token) {
        String accountNumber = authorizationTokenDb.getAccountNumberByToken(token);
        log.info("Sending balance check call to bank-service client");
        BigDecimal balance = bankServiceClient.balance(accountNumber);
        log.info("Balance check call has been completed Successfully");
        return balance;
    }

    @Override
    public List<UserTransactionDto> print(String token, Instant startDate, Instant endDate) {
        String accountNumber = authorizationTokenDb.getAccountNumberByToken(token);
        log.info("Attempting to generate receipt for user");
        List<UserTransactionDto> transactions = bankServiceClient.printTransactions(accountNumber,startDate,endDate);
        log.info("Successfully generated receipt for user");
        return transactions;
    }
}
