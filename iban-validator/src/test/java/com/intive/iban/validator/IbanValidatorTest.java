package com.intive.iban.validator;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RunWith(DataProviderRunner.class)
public class IbanValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @UseDataProvider(value = "dataProviderValidIban", location = DataProviderIbanTest.class)
    public void testValidIban(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        Assert.assertTrue(iban.isIbanValid());
    }

    @Test
    @UseDataProvider(value = "dataProviderInvalidIbanBadCountryCodeOrLength", location = DataProviderIbanTest.class)
    public void testInvalidIbanBadCountryCodeOrLength(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        Assert.assertFalse(iban.isIbanValid());
    }

    @Test
    @UseDataProvider(value = "dataProviderInvalidIbanBadFormat", location = DataProviderIbanTest.class)
    public void testInvalidIbanBadFormat(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        Assert.assertFalse(iban.isIbanValid());
    }

    @Test
    @UseDataProvider(value = "dataProviderInvalidIbanUnallowedCharacters", location = DataProviderIbanTest.class)
    public void testInvalidIbanUnallowedCharacters(String ibanData) {
        SampleIban iban = new SampleIban(ibanData);
        Assert.assertFalse(iban.isIbanValid());
    }

    public class SampleIban {

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
