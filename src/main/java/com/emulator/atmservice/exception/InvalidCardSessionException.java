package com.emulator.atmservice.exception;

public class InvalidCardSessionException extends RuntimeException {
    public InvalidCardSessionException(String message) {
        super(message);
    }
}
