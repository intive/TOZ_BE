package com.intive.patronage.toz.util.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotPastValidator implements ConstraintValidator<NotPast, LocalDate> {

    private static final String NULL_MESSAGE = "may not be null";

    @Override
    public void initialize(NotPast constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    NULL_MESSAGE)
                    .addConstraintViolation();
            return false;
        }
        LocalDate today = LocalDate.now();
        return value.isEqual(today) || value.isAfter(today);
    }
}
