package com.intive.iban.validator;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<IbanFormat, String> {

    private final String COUNTRY_CODE = "PL";

    public void initialize(IbanFormat constraintAnnotation) {
    }

    public boolean isValid(String iban, ConstraintValidatorContext context) {
        try {
            IbanUtil.validate(COUNTRY_CODE + iban);
            return true;
        } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
            setErrorMessage(context, e.getMessage());
            return false;
        }
    }

    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
