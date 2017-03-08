package com.intive.patronage.toz.exception;

public class ArgumentNotValidException extends RuntimeException {
    private final static String INVALID_ARGUMENT = "Invalid argument: ";

    public ArgumentNotValidException(String argument) {
        super(INVALID_ARGUMENT + argument);
    }
}
