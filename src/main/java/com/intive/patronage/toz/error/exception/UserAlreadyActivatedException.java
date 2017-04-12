package com.intive.patronage.toz.error.exception;

import com.intive.patronage.toz.users.model.db.User;

public class UserAlreadyActivatedException extends RuntimeException {
    private final String name;

    public UserAlreadyActivatedException(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
