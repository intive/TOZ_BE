package com.intive.patronage.toz.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PetValues {

    public static final String PATH = "/pets";

    public enum Type {
        DOG, CAT;

        @JsonCreator
        public static Type fromString(String key) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Sex {
        MALE, FEMALE, UNKNOWN;

        @JsonCreator
        public static Sex fromString(String key) {
            for (Sex sex : values()) {
                if (sex.name().equalsIgnoreCase(key)) {
                    return sex;
                }
            }
            return null;
        }
    }
}
