package com.intive.patronage.toz.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotPastValidator implements ConstraintValidator<NotPast, LocalDate> {

    @Override
    public void initialize(NotPast constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        return date == null || date.isEqual(today) || date.isAfter(today);
    }
}
