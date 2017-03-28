package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel("Schedule")
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScheduleView {
    private final List<ReservationView> reservations;
    private final List<DayConfigView> configs;

    public ScheduleView(List<ReservationView> reservations, List<DayConfigView> configs) {
        this.reservations = reservations;
        this.configs = configs;
    }
}
