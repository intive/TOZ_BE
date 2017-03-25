package com.intive.patronage.toz.error;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private final HttpStatus status;
    private final String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return status.value();
    }

    public String getError() {
        return status.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }
}
