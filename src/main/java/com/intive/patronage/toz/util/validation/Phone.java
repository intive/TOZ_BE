package com.intive.patronage.toz.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Phone {

    String message() default "value not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
