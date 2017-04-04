package com.intive.patronage.toz.error.model;

import org.springframework.http.HttpStatus;

public class ArgumentErrorResponse extends ErrorResponse {

    private final String argument;
    private final String value;

    public ArgumentErrorResponse(HttpStatus status, String message, String argument, String value) {
        super(status, message);

        this.argument = argument;
        this.value = value;
    }

    public String getArgument() {
        return argument;
    }

    public String getValue() {
        return value;
    }
}
