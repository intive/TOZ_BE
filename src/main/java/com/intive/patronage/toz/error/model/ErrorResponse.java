package com.intive.patronage.toz.error.model;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final Date timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        timestamp = new Date();
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

    public Date getTimestamp() {
        return timestamp;
    }
}
