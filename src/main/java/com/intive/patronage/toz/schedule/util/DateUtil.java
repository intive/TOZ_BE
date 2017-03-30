package com.intive.patronage.toz.schedule.util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {

    public static Date convertToDate(LocalDate localDate, LocalTime localTime, ZoneOffset zoneOffset) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.toInstant(zoneOffset));
    }

    public static Date convertToDate(String localDate, String localTime, ZoneOffset zoneOffset) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(localDate), LocalTime.parse(localTime));
        return Date.from(localDateTime.toInstant(zoneOffset));
    }
}
