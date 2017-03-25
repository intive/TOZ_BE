package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@ApiModel("Day configuration")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class DayConfig {

    @ApiModelProperty(value = "Name of day")
    private DayOfWeek dayOfWeek;
    @ApiModelProperty(value = "List of periods in a day")
    private List<Period> periods;

    @ApiModelProperty(value = "Number of periods in a day")
    public int getNumberOfPeriods() {
        return periods.size();
    }
}
