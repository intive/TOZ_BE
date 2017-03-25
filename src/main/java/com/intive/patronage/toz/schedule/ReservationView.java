package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.model.db.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ApiModel("Reservation")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ReservationView extends IdentifiableView {

    @ApiModelProperty(value = "Date in UTC", required = true)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "13:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    private LocalTime startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    private LocalTime endTime;

    @ApiModelProperty(value = "Owner", required = true)
    @Valid
    @NotNull
    private UserView owner;

    private LocalDateTime creationTime;

    private LocalDateTime modificationDate;
    private String modificationMessage;
    private UserView modificationAuthor;
}
