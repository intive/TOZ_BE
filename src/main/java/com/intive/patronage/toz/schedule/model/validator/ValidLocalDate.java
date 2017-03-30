package com.intive.patronage.toz.schedule.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocalDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidLocalDate {

    String message() default "Local date value is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
