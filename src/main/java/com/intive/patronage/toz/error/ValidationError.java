package com.intive.patronage.toz.error;

public class ValidationError {

    private final int code;
    private final String field;
    private final String message;

    public ValidationError(int code, String field, String message) {
        this.code = code;
        this.field = field;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
