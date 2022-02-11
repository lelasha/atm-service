package com.emulator.atmservice.db;

public class CardHasBeenBlockedException extends RuntimeException {
    public CardHasBeenBlockedException(String message) {
        super(message);
    }
}
