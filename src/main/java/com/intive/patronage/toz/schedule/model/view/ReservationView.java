package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.schedule.model.validator.ValidLocalDate;
import com.intive.patronage.toz.schedule.model.validator.ValidLocalTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Reservation")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class ReservationView {

    @ApiModelProperty(value = "Date in UTC", example = "2017-01-20", required = true, position = 1)
    @NotNull
    @ValidLocalDate
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private String date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "08:00", position = 2)
    @NotNull
    @ValidLocalTime
    private String startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:00", position = 3)
    @NotNull
    @ValidLocalTime
    private String endTime;

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true, position = 4)
    @Valid
    @NotNull
    private UUID ownerId;

    @ApiModelProperty(value = "Modification message", position = 7)
    private String modificationMessage;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", position = 8)
    private UUID modificationAuthorId;
}
