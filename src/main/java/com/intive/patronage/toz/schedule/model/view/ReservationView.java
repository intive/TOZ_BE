package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
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
@ApiModel(value = "Reservation")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class ReservationView {

    @Getter(AccessLevel.NONE)
    private final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    @Getter(AccessLevel.NONE)
    private final String LOCAL_TIME_PATTERN = "HH:mm";

    @ApiModelProperty(value = "Date in UTC", example = "2017-10-20", required = true, position = 1)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private LocalDate date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "08:00", position = 2)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = LOCAL_TIME_PATTERN)
    private LocalTime startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:00", position = 3)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = LOCAL_TIME_PATTERN)
    private LocalTime endTime;

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true, position = 4)
    @Valid
    @NotNull
    private UUID ownerId;

    @ApiModelProperty(value = "Modification message", position = 7)
    private String modificationMessage;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", position = 8)
    private UUID modificationAuthorId;
}
