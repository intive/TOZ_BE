package com.intive.patronage.toz.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern PATTERN_9_OR_11_DIGITS = Pattern.compile("\\d{9}(\\d{2})?");

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber != null && PATTERN_9_OR_11_DIGITS.matcher(phoneNumber).matches();
    }

}
