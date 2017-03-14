package com.intive.patronage.toz.error;

public class ErrorResponse {

    private final int code;
    private final String error;
    private final String message;

    public ErrorResponse(int code, String error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
