package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@ApiModel("Period")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Period {

    @ApiModelProperty(value = "Start time in UTC", example = "08:00")
    private LocalTime periodStart;
    @ApiModelProperty(value = "End time in UTC", example = "17:00")
    private LocalTime periodEnd;
}
