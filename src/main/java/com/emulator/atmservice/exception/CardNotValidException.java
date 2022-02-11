package com.emulator.atmservice.exception;

import lombok.Getter;

@Getter
public class CardNotValidException extends RuntimeException {
    public CardNotValidException(String message) {
        super(message);
    }
}
