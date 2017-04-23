package com.intive.patronage.toz.schedule.constant;

import java.time.format.DateTimeFormatter;

public final class LocalDateTimeFormat {

    private static final String LOCAL_TIME_FORMAT = "HH:mm";
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    private LocalDateTimeFormat() {
    }

    public static DateTimeFormatter getInstanceOfLocalTimeFormatter() {
        return DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT);
    }

    public static DateTimeFormatter getInstanceOfLocalDateFormatter() {
        return DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
    }
}
