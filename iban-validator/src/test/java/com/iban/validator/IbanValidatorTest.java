package com.iban.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.*;
import java.util.Set;

public class IbanValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidIban() {
        SampleIban iban1 = new SampleIban("AL47 2121 1009 0000 0002 3569 8741");
        SampleIban iban2 = new SampleIban("AT611904300234573201");
        SampleIban iban3 = new SampleIban("DE89 3704 0044 0532 0130 00");
        SampleIban iban4 = new SampleIban("PL60 1020 1026 0000 0422 7020 1111");

        Assert.assertTrue(iban1.isIbanValid());
        Assert.assertTrue(iban2.isIbanValid());
        Assert.assertTrue(iban3.isIbanValid());
        Assert.assertTrue(iban4.isIbanValid());
    }

    @Test
    public void testInvalidIbanBadCountryCodeOrLength() {
        SampleIban iban1 = new SampleIban("XX47 2121 1009 0000 0002 3569 8741");
        SampleIban iban2 = new SampleIban("AT6011904300234573201");
        SampleIban iban3 = new SampleIban("DE89 3704 0044 0532 0130 0");
        SampleIban iban4 = new SampleIban("2260 1020 1026 0000 0422 7020 1111");

        Assert.assertFalse(iban1.isIbanValid());
        Assert.assertFalse(iban2.isIbanValid());
        Assert.assertFalse(iban3.isIbanValid());
        Assert.assertFalse(iban4.isIbanValid());
    }

    @Test
    public void testInvalidIbanBadFormat() {
        SampleIban iban1 = new SampleIban("AL47 2121 1009 00 00 0002 3569 8741");
        SampleIban iban2 = new SampleIban(" AT611904300234573201");
        SampleIban iban3 = new SampleIban("DE89 3704 0044 05320130 00");
        SampleIban iban4 = new SampleIban("PL60 1020 1026 0000 0422 7020 111 1");
        SampleIban iban5 = new SampleIban(null);
        SampleIban iban6 = new SampleIban("");
        SampleIban iban7 = new SampleIban(" ");

        Assert.assertFalse(iban1.isIbanValid());
        Assert.assertFalse(iban2.isIbanValid());
        Assert.assertFalse(iban3.isIbanValid());
        Assert.assertFalse(iban4.isIbanValid());
        Assert.assertFalse(iban5.isIbanValid());
        Assert.assertFalse(iban6.isIbanValid());
        Assert.assertFalse(iban7.isIbanValid());
    }

    @Test
    public void testInvalidIbanUnallowedCharacters() {
        SampleIban iban1 = new SampleIban("AL47 \n21 1009 0000 0002 3569 8741");
        SampleIban iban2 = new SampleIban("AT61.904300234573201");
        SampleIban iban3 = new SampleIban("DE89-3704-0044-0532-0130-00");
        SampleIban iban4 = new SampleIban("PL60 \\n+ 1026 0000 0422 7020 1111");

        Assert.assertFalse(iban1.isIbanValid());
        Assert.assertFalse(iban2.isIbanValid());
        Assert.assertFalse(iban3.isIbanValid());
        Assert.assertFalse(iban4.isIbanValid());
    }

    public class SampleIban {

        @Iban
        String iban;

        public SampleIban(String iban) {
            this.iban = iban;
        }

        public boolean isIbanValid() {
            Set<ConstraintViolation<SampleIban>> violations = validator.validate(this);
            return !(violations.size() > 0);
        }
    }
}
