package com.intive.patronage.toz.schedule.util;

import com.intive.patronage.toz.schedule.excception.InvalidReservationHoursException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;

@Getter
@Setter
@Component
@PropertySource("classpath:schedule.properties")
public class ScheduleParser {

    private EnumMap<DayOfWeek, String[]> schedule;

    public ScheduleParser(@Value("${MONDAY}") String[] mondaySchedule,
                          @Value("${TUESDAY}") String[] tuesdaySchedule,
                          @Value("${WEDNESDAY}") String[] wednesdaySchedule,
                          @Value("${THURSDAY}") String[] thursdaySchedule,
                          @Value("${FRIDAY}") String[] fridaySchedule,
                          @Value("${SATURDAY}") String[] saturdaySchedule,
                          @Value("${SUNDAY}") String[] sundaySchedule) {
        schedule = new EnumMap<DayOfWeek, String[]>(DayOfWeek.class);
        schedule.put(DayOfWeek.MONDAY, mondaySchedule);
        schedule.put(DayOfWeek.TUESDAY, tuesdaySchedule);
        schedule.put(DayOfWeek.WEDNESDAY, wednesdaySchedule);
        schedule.put(DayOfWeek.THURSDAY, thursdaySchedule);
        schedule.put(DayOfWeek.FRIDAY, fridaySchedule);
        schedule.put(DayOfWeek.SATURDAY, saturdaySchedule);
        schedule.put(DayOfWeek.SUNDAY, sundaySchedule);
    }

    public void validateHours(Date dateFrom, Date dateTo) {
        LocalTime from = dateFrom.toInstant().atOffset(ZoneOffset.UTC).toLocalTime();
        LocalTime to = dateTo.toInstant().atOffset(ZoneOffset.UTC).toLocalTime();
        DayOfWeek day = dateFrom.toInstant().atOffset(ZoneOffset.UTC).toLocalDate().getDayOfWeek();
        String hours = String.format("%s-%s", from, to);
        if (!Arrays.asList(schedule.get(day)).contains(hours)) {
            throw new InvalidReservationHoursException(hours, day);
        }
    }
}
