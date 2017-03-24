package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class PeriodConfig {

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer periodDuration;
    private Integer amountOfPeriods;
}
