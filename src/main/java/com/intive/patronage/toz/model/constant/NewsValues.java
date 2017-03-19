package com.intive.patronage.toz.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NewsValues {
    public static final String PATH = "/news";

    public enum Type {
        RELEASED, UNRELEASED;

        @JsonCreator
        public static NewsValues.Type fromString(String key) {
            for (NewsValues.Type type : values()) {
                if (type.name().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }
}
