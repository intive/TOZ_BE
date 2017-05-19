package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.util.validation.NotPast;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "ScheduleReservation")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class ReservationView {

    @ApiModelProperty(value = "Date in UTC", example = "2017-01-20", required = true, position = 1)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotPast
    private LocalDate date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "08:00", position = 2)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:00", position = 3)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true, position = 4)
    @Valid
    @NotNull
    private UUID ownerId;
}
