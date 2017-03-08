package com.iban.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IbanValidator implements ConstraintValidator<Iban, String> {

    private String iban;
    private String countryCode;

    public void initialize(Iban constraintAnnotation) {
    }

    public boolean isValid(String iban, ConstraintValidatorContext context) {
        this.iban = iban;
        if (isNullOrEmpty() || hasNonAlphanumericCharactersExceptSpaces() ||
                hasInvalidSpaces() || hasInvalidCountryCodeOrLength())
            return false;

        return true;
    }

    private boolean isNullOrEmpty() {
        return iban == null || iban.equals("");
    }

    private boolean hasNonAlphanumericCharactersExceptSpaces() {
        Pattern nonAlphanumericExceptSpaces = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher matcher = nonAlphanumericExceptSpaces.matcher(iban);
        return matcher.find();
    }

    private boolean hasInvalidSpaces() {
        if (hasNoSpaces() || hasValidFormat())
            return false;

        return true;
    }

    private boolean hasInvalidCountryCodeOrLength() {
        getCountryCodeFromIban();
        return hasInvalidCountryCode() || hasInvalidLength();
    }

    private boolean hasNoSpaces() {
        Pattern spaces = Pattern.compile("[ ]");
        Matcher matcher = spaces.matcher(iban);
        return !matcher.find();
    }

    private boolean hasValidFormat() {
        return iban.matches("^(([A-Za-z0-9]){4} )+[A-Za-z0-9]{1,4}$");
    }

    private boolean hasInvalidCountryCode() {
        return !IbanCountryCodeAndLength.isCountryCodeValid(countryCode);
    }

    private boolean hasInvalidLength() {
        String ibanWithoutSpaces = iban.replace(" ", "");
        return ibanWithoutSpaces.length() != IbanCountryCodeAndLength.getLengthByCountryCodeOrNull(countryCode);
    }

    private void getCountryCodeFromIban() {
        this.countryCode = iban.substring(0, 2);
    }
}
