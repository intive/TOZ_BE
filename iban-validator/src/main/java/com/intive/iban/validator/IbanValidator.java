package com.intive.iban.validator;

import org.iban4j.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<IBAN, String> {

    public void initialize(IBAN constraintAnnotation) {
    }

    public boolean isValid(String iban, ConstraintValidatorContext context) {
        try {
            IbanUtil.validate(iban);
            return true;
        } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
            String errorMessage = String.format("{com.intive.iban.validator.IbanValidator.%s}", e.getMessage());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }
    }
}
