package com.iban.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<Iban, String>{

    public void initialize(Iban constraintAnnotation) {}

    public boolean isValid(String iban, ConstraintValidatorContext context) {
        return false;
    }
}
