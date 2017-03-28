package com.intive.patronage.toz.schedule;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {

    //TODO: move this to configuration
    private static ZoneOffset timeZoneOffset = ZoneOffset.UTC;

    static Date convertToDate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.toInstant(timeZoneOffset));
    }
}
