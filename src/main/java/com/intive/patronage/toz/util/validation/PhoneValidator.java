package com.intive.patronage.toz.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }
        int numberLength = phoneNumber.length(); // TODO pattern regex
        return numberLength == 9 || numberLength == 11;
    }

}
