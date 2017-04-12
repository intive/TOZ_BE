package com.intive.patronage.toz.schedule.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("ScheduleReservation Response")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReservationResponseView extends ReservationView {

    @ApiModelProperty(example = "c5296892-347f-4b2e-b1c6-6faff971f767")
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(example = "1490134074968", position = 5)
    private Long created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(example = "1490134074968", position = 6)
    private Long lastModified;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", position = 8)
    private UUID modificationAuthorId;
}
