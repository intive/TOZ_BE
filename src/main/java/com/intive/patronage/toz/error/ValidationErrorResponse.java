package com.intive.patronage.toz.error;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public class ValidationErrorResponse {

    private final int code;
    private final String message;
    private final List<FieldError> errors;

    public ValidationErrorResponse(int code, String message, MethodArgumentNotValidException ex) {
        this.code = code;
        this.message = message;
        this.errors = setErrors(ex);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    private List<FieldError> setErrors(MethodArgumentNotValidException ex) {
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error);
        }
        return errors;
    }
}
