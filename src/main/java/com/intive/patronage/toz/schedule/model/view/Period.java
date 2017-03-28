package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@ApiModel("Period")
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Period {

    @ApiModelProperty(value = "Start time in UTC", example = "08:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private final LocalTime periodStart;

    @ApiModelProperty(value = "End time in UTC", example = "17:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private final LocalTime periodEnd;

    public Period(LocalTime periodStart, LocalTime periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }
}
