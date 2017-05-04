package com.intive.patronage.toz.error.exception;

import com.intive.patronage.toz.users.model.db.User;

public class BadRoleForNewUserException extends RuntimeException {

    private final User.Role newUserRole;

    public BadRoleForNewUserException(User.Role newUserRole) {
        this.newUserRole = newUserRole;
    }

    public User.Role getNewUserRole() {
        return newUserRole;
    }
}
