package com.intive.patronage.toz.util.validation;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.util.UserInfoGetter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotPastValidator implements ConstraintValidator<NotPast, LocalDate> {

    @Override
    public void initialize(NotPast constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        return UserInfoGetter.hasCurrentUserAdminRole() || date == null || date.isEqual(today) || date.isAfter(today);
    }
}
