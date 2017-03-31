package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ArgumentErrorResponse;
import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.exception.AlreadyExistsException;
import com.intive.patronage.toz.exception.InvalidImageFileException;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EnableWebMvc
@ControllerAdvice
class ControllerExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private final MessageSource messageSource;

    @Autowired
    ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleRuntimeException(RuntimeException e) {
        String errorLog = String.format("%s, ID: %s", e.getMessage(), UUID.randomUUID().toString());
        logger.error(errorLog);
        return errorLog;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        String message = messageSource.getMessage("validationError",
                null,
                LocaleContextHolder.getLocale());
        List<ValidationErrorResponse.SingleFieldError> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(new ValidationErrorResponse.SingleFieldError(error.getField(),
                    error.getDefaultMessage(),
                    String.valueOf(error.getRejectedValue())));
        }
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), message, errors);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleAlreadyExistsException(AlreadyExistsException e) {
        String message = messageSource.getMessage("alreadyExists",
                new String[]{e.getName()},
                LocaleContextHolder.getLocale());

        return new ErrorResponse(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        String message = messageSource.getMessage("notFound",
                new String[]{e.getName()},
                LocaleContextHolder.getLocale());

        return new ErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ArgumentErrorResponse handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = messageSource.getMessage("invalidArgumentValue",
                null,
                LocaleContextHolder.getLocale());

        return new ArgumentErrorResponse(HttpStatus.BAD_REQUEST, message, e.getName(), e.getValue().toString());
    }

    @ExceptionHandler(InvalidImageFileException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse invalidImageFileException(InvalidImageFileException e) {
        String message = messageSource.getMessage("unprocessableEntity",
                new String[]{e.getMessage()},
                LocaleContextHolder.getLocale());
        logger.error(message);
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
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
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
    }
}
