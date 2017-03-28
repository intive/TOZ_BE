package com.intive.patronage.toz.exception;

import java.util.Arrays;

public class WrongEnumValueException extends RuntimeException {

    private final Class<? extends Enum> enumType;

    public WrongEnumValueException(Class<? extends Enum> enumType) {
        this.enumType = enumType;
    }

    public String getName() {
        return enumType.getSimpleName().toLowerCase();
    }

    public String[] getAllowedValues() {
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
