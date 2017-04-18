package com.intive.patronage.toz.schedule.util;

import com.intive.patronage.toz.schedule.excception.InvalidReservationHoursException;
import com.intive.patronage.toz.schedule.model.view.DayConfigView;
import com.intive.patronage.toz.schedule.model.view.PeriodView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Getter
@Setter
@Component
@PropertySource("classpath:schedule.properties")
public class ScheduleParser {

    private EnumMap<DayOfWeek, String[]> schedule;
    public static final String HOURS_SEPARATOR = "-";

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

    public List<DayConfigView> getDaysConfig() {
        List<DayConfigView> dayConfigs = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String day = dayOfWeek.toString();
            dayConfigs.add(new DayConfigView(
                    day,
                    getPeriodsForDay(dayOfWeek)));
        }
        return dayConfigs;
    }

    private List<PeriodView> getPeriodsForDay(DayOfWeek dayOfWeek) {
        List<PeriodView> periods = new ArrayList<>();
        for (String dayHours : schedule.get(dayOfWeek)) {
            String[] hours = dayHours.split(HOURS_SEPARATOR);
            periods.add(new PeriodView(hours[0], hours[1]));
        }
        return periods;
    }
}
