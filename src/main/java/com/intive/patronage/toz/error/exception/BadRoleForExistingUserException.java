package com.intive.patronage.toz.error.exception;

import com.intive.patronage.toz.users.model.db.User;

public class BadRoleForExistingUserException extends RuntimeException {

    private final User.Role userRole;

    public BadRoleForExistingUserException(User.Role userRole) {
        this.userRole = userRole;
    }

    public User.Role getUserRole() {
        return userRole;
    }
}
