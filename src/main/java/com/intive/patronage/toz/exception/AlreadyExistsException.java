package com.intive.patronage.toz.exception;

public class AlreadyExistsException extends RuntimeException {

    private final String name;

    public AlreadyExistsException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
