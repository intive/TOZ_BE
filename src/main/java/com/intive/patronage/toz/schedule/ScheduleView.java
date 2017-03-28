package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel("Schedule")
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ScheduleView {
    private final List<ReservationView> reservations;
    private final List<DayConfigView> configs;

    ScheduleView(List<ReservationView> reservations, List<DayConfigView> configs) {
        this.reservations = reservations;
        this.configs = configs;
    }
}
