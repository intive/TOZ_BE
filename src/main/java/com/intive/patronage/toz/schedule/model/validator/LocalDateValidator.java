package com.intive.patronage.toz.schedule.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.intive.patronage.toz.schedule.constant.DateTimeConsts.LOCAL_DATE_REGEX;

public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {

    @Override
    public void initialize(ValidLocalDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!value.matches(LOCAL_DATE_REGEX)){
            return false;
        }
        try {
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
