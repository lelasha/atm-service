package com.emulator.atmservice.exception;

public class InvalidAuthorizationTokenException extends RuntimeException {
    public InvalidAuthorizationTokenException(String message) {
        super(message);
    }
}
