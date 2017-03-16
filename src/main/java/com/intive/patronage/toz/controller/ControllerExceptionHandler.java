package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.exception.AlreadyExistsException;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@EnableWebMvc
@ControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleAlreadyExistsException(AlreadyExistsException e) {
        String message = messageSource.getMessage("alreadyExists",
                new String[]{e.getName()},
                LocaleContextHolder.getLocale());

        return new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), message);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        String message = messageSource.getMessage("notFound",
                new String[]{e.getName()},
                LocaleContextHolder.getLocale());

        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), message);
    }

    @ExceptionHandler(WrongEnumValueException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleWrongEnumValueException(WrongEnumValueException e) {
        final String typeName = e.getName();
        final String allowedValues = StringUtils.join(Arrays.asList(e.getAllowedValues()), ", ");
        final String message = messageSource.getMessage("mustHaveValue",
                new String[]{typeName, allowedValues},
                LocaleContextHolder.getLocale());
        final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

    public static class ErrorResponse {
        private final int code;
        private final String error;
        private final String message;

        ErrorResponse(int code, String error, String message) {
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
}
