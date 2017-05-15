package com.intive.patronage.toz.error.exception;

import com.intive.patronage.toz.users.model.db.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WrongProposalRoleException extends RuntimeException {

    private final static Set<Role> allowedValues = new HashSet<>(Arrays.asList(Role.VOLUNTEER));

    public String getAllowedValues() {
        StringBuilder stringBuilder = new StringBuilder();
        allowedValues.forEach(
                role->stringBuilder.append(String.format("%s ", role))
        );
        return stringBuilder.toString();
    }
}
