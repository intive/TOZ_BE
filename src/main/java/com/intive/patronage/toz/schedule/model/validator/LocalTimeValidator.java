package com.intive.patronage.toz.schedule.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.intive.patronage.toz.schedule.constant.DateTimeConsts.HOURS_24_REGEX;

public class LocalTimeValidator implements ConstraintValidator<ValidLocalTime, String> {

    @Override
    public void initialize(ValidLocalTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(HOURS_24_REGEX);
    }
}
