package com.intive.patronage.toz.error.model;

import java.util.Date;
import java.util.List;

public class ValidationErrorResponse {

    private final int code;
    private final String message;
    private final List<SingleFieldError> errors;

    public ValidationErrorResponse(int code, String message, List<SingleFieldError> errors) {
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

    public List<SingleFieldError> getErrors() {
        return errors;
    }

    public Date getTimestamp() {
        return new Date();
    }

    public static class SingleFieldError {

        private final String field;
        private final String message;
        private final String rejectedValue;

        public SingleFieldError(String field, String message, String rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public String getRejectedValue() {
            return rejectedValue;
        }
    }
}
