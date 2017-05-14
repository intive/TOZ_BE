package com.intive.patronage.toz.error.exception;

public class WrongProposalRoleException extends RuntimeException {

    private final static String[] allowedValues = new String[]{"VOLUNTEER","TEMP_HOUSE"};

    public String[] getAllowedValues() {
        return allowedValues;
    }
}
