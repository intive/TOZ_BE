package com.intive.patronage.toz.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumValidate {

    Class<? extends Enum> enumClass();

    String message() default "value not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
