package com.intive.iban.validator;

import org.iban4j.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.*;
import java.util.Properties;

public class IbanValidator implements ConstraintValidator<IbanFormat, String> {

    private String COUNTRY_CODE;
    private final String DEFAULT_COUNTRY_CODE = "PL";

    public void initialize(IbanFormat constraintAnnotation) {
        getCountryCodeFromConfig();
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

    private void getCountryCodeFromConfig() {
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream("src\\main\\resources\\application.properties");
            prop.load(input);
            COUNTRY_CODE = prop.getProperty("COUNTRY_CODE");
        } catch (IOException e) {
            COUNTRY_CODE = DEFAULT_COUNTRY_CODE;
        }
    }

    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        String errorMessage = String.format("{com.intive.iban.validator.IbanValidator.%s}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
    }
}
