package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.intive.patronage.toz.model.db.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@ApiModel("Reservation")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = ReservationView.ReservationViewBuilder.class)
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

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true)
    @Valid
    @NotNull
    private UUID ownerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime lastModified;

    @ApiModelProperty(value = "Modification message")
    private String modificationMessage;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767")
    private UUID modificationAuthorId;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class ReservationViewBuilder {
    }

}
