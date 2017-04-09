package com.intive.patronage.toz.schedule.constant;

import java.util.regex.Pattern;

public final class DateTimePattern {

    private static final Pattern HOURS_24_REGEX = Pattern.compile("([01]+[0-9]|2[0-3]):[0-5][0-9]");
    private static final Pattern LOCAL_DATE_REGEX = Pattern.compile("[\\d]{4}-[\\d]{2}-[\\d]{2}");

    public static Pattern localTime24(){
        return HOURS_24_REGEX;
    }

    public static Pattern localDate(){
        return LOCAL_DATE_REGEX;
    }

    private DateTimePattern() {
    }
}
