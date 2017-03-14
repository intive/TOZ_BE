package com.intive.patronage.toz.error;

import java.util.List;

public class ValidationErrorResponse {

    private final int code;
    private final String message;
    private final List<ValidationError> errors;

    public ValidationErrorResponse(int code, String message, List<ValidationError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
