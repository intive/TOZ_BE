package com.iban.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
@Documented
public @interface Iban {

    String message() default "{com.iban.validator.IbanValidator." + "invalidIbanFormat}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
