package com.intive.patronage.toz.schedule.excception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
public class InvalidReservationHoursException extends ScheduleException {

    private String invalidHours;
    private DayOfWeek day;
}
