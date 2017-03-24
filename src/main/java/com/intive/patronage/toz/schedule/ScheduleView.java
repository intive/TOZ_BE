package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ScheduleView {

    private List<ReservationView> reservations;
    private PeriodConfig config;
}
