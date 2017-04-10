package com.intive.patronage.toz.error.exception;

public class ActivationExpiredException extends RuntimeException {

    private final String name;

    public ActivationExpiredException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
