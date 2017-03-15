package com.intive.patronage.toz.exception;

public class InvalidImageFileException extends RuntimeException {

    private final String name;

    public InvalidImageFileException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
