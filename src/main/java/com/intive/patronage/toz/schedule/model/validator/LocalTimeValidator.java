package com.intive.patronage.toz.schedule.model.validator;

import com.intive.patronage.toz.schedule.constant.DateTimePattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocalTimeValidator implements ConstraintValidator<ValidLocalTime, String> {

    @Override
    public void initialize(ValidLocalTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return DateTimePattern.localTime24().matcher(value).matches();
    }
}
