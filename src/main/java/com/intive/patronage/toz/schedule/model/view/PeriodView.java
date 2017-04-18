package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel("Period")
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PeriodView {

    @ApiModelProperty(value = "Start time in UTC", example = "08:00")
    private final String periodStart;

    @ApiModelProperty(value = "End time in UTC", example = "17:00")
    private final String periodEnd;

    public PeriodView(String periodStart, String periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }
}
