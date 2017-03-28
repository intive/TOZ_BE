package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class ReservationView {

    @ApiModelProperty(value = "Date in UTC", required = true)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "13:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = "HH:mm", timezone = "UTC")
    private LocalTime startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = "HH:mm", timezone = "UTC")
    private LocalTime endTime;

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true)
    @Valid
    @NotNull
    private UUID ownerId;

    @ApiModelProperty(value = "Modification message")
    private String modificationMessage;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767")
    private UUID modificationAuthorId;
}
