package com.intive.patronage.toz.error.exception;

import com.intive.patronage.toz.users.model.db.Role;

public class BadRoleForSentUserBodyException extends RuntimeException {

    private final Role userRole;

    public BadRoleForSentUserBodyException(Role userRole) {
        this.userRole = userRole;
    }

    public Role getUserRole() {
        return userRole;
    }
}
