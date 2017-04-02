package com.intive.patronage.toz.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValidate, String> {

    private Enum[] enumValues;

    @Override
    public void initialize(EnumValidate constraintAnnotation) {
        enumValues = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String requestValue, ConstraintValidatorContext context) {
        for (Enum e : enumValues) {
            String enumValue = e.toString();
            if (enumValue.equals(requestValue)) {
                return true;
            }
        }
        return requestValue == null;
    }
}
