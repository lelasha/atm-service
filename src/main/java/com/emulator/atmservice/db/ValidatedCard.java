package com.emulator.atmservice.db;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class ValidatedCard {
    private String cardNumber;
    private String accountNumber;
    private Instant cuttingPointForNumberOfTries;
    private AtomicInteger attemptedTriesCounter;

    public ValidatedCard(String cardNumber,
                         String accountNumber,
                         Instant cuttingPointForNumberOfTries,
                         AtomicInteger attemptedTriesCounter) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.cuttingPointForNumberOfTries = cuttingPointForNumberOfTries;
        this.attemptedTriesCounter = new AtomicInteger();
    }
}
