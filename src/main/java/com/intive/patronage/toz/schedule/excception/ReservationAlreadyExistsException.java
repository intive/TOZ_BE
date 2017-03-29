package com.intive.patronage.toz.schedule.excception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReservationAlreadyExistsException extends ScheduleException {

    private LocalTime invalidTime;
    private LocalDate day;
}
