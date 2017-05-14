package com.intive.patronage.toz.error;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.InvalidImageFileException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.error.exception.WrongEnumValueException;
import com.intive.patronage.toz.error.exception.WrongProposalRoleException;
import com.intive.patronage.toz.error.exception.*;
import com.intive.patronage.toz.error.model.ArgumentErrorResponse;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.schedule.excception.InvalidReservationHoursException;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.mail.MessagingException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EnableWebMvc
@ControllerAdvice
public class ControllerExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private final MessageSource messageSource;
    private final ScheduleParser scheduleParser;

    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource, ScheduleParser scheduleParser) {
        this.messageSource = messageSource;
        this.scheduleParser = scheduleParser;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleRuntimeException(RuntimeException e) {
        StringWriter stackTraceWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTraceWriter));
        String errorLog =
                String.format("%s, ID: %s, %s",
                        e.getMessage(),
                        UUID.randomUUID().toString(),
                        stackTraceWriter.toString());
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

    @ExceptionHandler(InvalidReservationHoursException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleInvalidReservationHoursException(InvalidReservationHoursException e) {
        final String allowedHours = Arrays.toString(scheduleParser.getSchedule().get(e.getDay()));
        final String day = e.getDay().toString();
        final String message = messageSource.getMessage("invalidScheduleHours",
                new String[]{e.getInvalidHours(), day, allowedHours},
                LocaleContextHolder.getLocale());
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleReservationAlreadyExistsException(ReservationAlreadyExistsException e) {
        final String message = messageSource.getMessage("reservationAlreadyExists",
                new String[]{e.getInvalidTime().toString(), e.getDay().toString()},
                LocaleContextHolder.getLocale());
        return new ErrorResponse(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleMessagingException(MessagingException e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        final String message = messageSource.getMessage(
                "accessDenied", null, LocaleContextHolder.getLocale());
        return new ErrorResponse(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(BadRoleForSentUserBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBadRoleForSentUserBodyException(BadRoleForSentUserBodyException e) {
        final String userRoleValue = e.getUserRole().toString();
        final String message = messageSource.getMessage(
                "badRoleForSentUserBody", new String[]{userRoleValue}, LocaleContextHolder.getLocale());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleWrongPasswordException(WrongPasswordException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(WrongProposalRoleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleWrongProposalRoleException(WrongProposalRoleException e) {
        final String allowedValues = StringUtils.join(Arrays.asList(e.getAllowedValues()), ", ");
        final String message = messageSource.getMessage("mustHaveValue",
                new String[]{"Role", allowedValues},
                LocaleContextHolder.getLocale());
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
