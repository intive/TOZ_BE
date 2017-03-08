package com.intive.patronage.toz.exception;

public class NotFoundException extends RuntimeException {
    private final static String NOT_FOUND = " not found!";

    public NotFoundException(String name) {
        super(name + NOT_FOUND);
    }
}
