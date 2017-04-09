package com.intive.patronage.toz.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Phone {

    String message() default "phone number should consist of 9 or 11 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
