package com.intive.patronage.toz.schedule.model.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ScheduleReservation Request")
@NoArgsConstructor
public class ReservationRequestView extends ReservationView {

    @ApiModelProperty(value = "Modification message", position = 7)
    private String modificationMessage;
}
