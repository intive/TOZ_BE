package com.intive.patronage.toz.schedule.model.validator;

import com.intive.patronage.toz.schedule.constant.DateTimePattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.intive.patronage.toz.schedule.constant.DateTimeFormat.LOCAL_DATE_FORMAT;

public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {

    @Override
    public void initialize(ValidLocalDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //TODO find other way to validate dates
        SimpleDateFormat dateFormat = new SimpleDateFormat(LOCAL_DATE_FORMAT);
        if (!DateTimePattern.localDate().matcher(value).matches()) {
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
