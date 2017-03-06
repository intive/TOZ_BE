package com.iban.validator;

import org.junit.Test;

public class IbanValidatorTest {

    @Test
    public void testValidIban(){

    }

    @Test
    public void testInvalidIbanBadCountryCodeOrLength(){

    }

    @Test
    public void testInvalidIbanUnallowedCharacters(){

    }

    public class IbanTester {

        @Iban
        String iban;

        public IbanTester(String iban) {
            this.iban = iban;
        }
    }
}
