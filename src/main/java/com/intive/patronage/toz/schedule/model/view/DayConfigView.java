package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;

@ApiModel("Day configuration")
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DayConfigView {

    @ApiModelProperty(value = "Name of day")
    private final DayOfWeek dayOfWeek;

    @ApiModelProperty(value = "List of periods in a day")
    private final List<PeriodView> periods;

    public DayConfigView(DayOfWeek dayOfWeek, List<PeriodView> periods) {
        this.dayOfWeek = dayOfWeek;
        this.periods = periods;
    }

    @ApiModelProperty(value = "Number of periods in a day")
    public int getNumberOfPeriods() {
        return periods.size();
    }
}
