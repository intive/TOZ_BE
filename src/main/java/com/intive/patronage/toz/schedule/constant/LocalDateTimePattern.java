package com.intive.patronage.toz.schedule.constant;

import java.util.regex.Pattern;

public final class LocalDateTimePattern {

    public static final Pattern HOURS_24_REGEX = Pattern.compile("([01]+[0-9]|2[0-3]):[0-5][0-9]");
    public static final Pattern LOCAL_DATE_REGEX = Pattern.compile("[\\d]{4}-[\\d]{2}-[\\d]{2}");
}
