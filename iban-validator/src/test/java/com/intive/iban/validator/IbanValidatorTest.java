package com.intive.iban.validator;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class IbanValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @DataProvider
    public static String[] dataProviderValidIban() {
        return new String[]{
                "60102010260000042270201111",
                " 61109010140000071219812874",
                "27114020040000300201355387 "};
    }

    @DataProvider
    public static String[] dataProviderInvalidIban() {
        return new String[]{
                "601020102600000422=70201111",
                "2 7114020040000300201355387",
                "\"61109010140000071219811\\\\n\"",
                "60-1020-1026-0000-0422-7020-1111",
                "",
                " ",
                null};
    }

    @Test
    @UseDataProvider("dataProviderValidIban")
    public void testValidIban(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        assertThat(iban.isIbanValid()).isTrue();
    }

    @Test
    @UseDataProvider("dataProviderInvalidIban")
    public void testInvalidIbanBadCountryCodeOrLength(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        assertThat(iban.isIbanValid()).isFalse();
    }

    private class SampleIban {

        @IbanFormat
        String iban;

        SampleIban(String iban) {
            this.iban = iban;
        }

        boolean isIbanValid() {
            Set<ConstraintViolation<SampleIban>> violations = validator.validate(this);
            return !(violations.size() > 0);
        }
    }
}
