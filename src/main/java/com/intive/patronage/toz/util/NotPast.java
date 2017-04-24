package com.intive.patronage.toz.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotPastValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotPast {

    String message() default "Date can't be in the past.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
