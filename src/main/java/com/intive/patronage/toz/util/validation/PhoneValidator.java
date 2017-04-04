package com.intive.patronage.toz.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        int phoneNumberLength = phoneNumber.length();
        return phoneNumberLength == 9 || phoneNumberLength == 11;
    }

}
