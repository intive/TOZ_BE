package com.iban.validator;

import org.junit.Before;
import org.junit.Test;
import javax.validation.*;

public class IbanValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidIban() {

    }

    @Test
    public void testInvalidIbanBadCountryCodeOrLength() {

    }

    @Test
    public void testInvalidIbanUnallowedCharacters() {

    }

    public class IbanTester {

        @Iban
        String iban;

        public IbanTester(@Iban String iban) {
            this.iban = iban;
        }
    }
}
