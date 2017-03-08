package com.intive.patronage.toz.exception;

public class AlreadyExistsException extends RuntimeException {
    private final static String ALREADY_EXISTS = " already exists!";

    public AlreadyExistsException(String name) {
        super("'" + name + "'" + ALREADY_EXISTS);
    }
}
