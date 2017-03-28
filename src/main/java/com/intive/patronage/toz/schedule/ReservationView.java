package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.db.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@ApiModel(value = "Reservation")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ReservationView extends IdentifiableView {

    private final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    private final String LOCAL_TIME_PATTERN = "HH:mm";

    @ApiModelProperty(value = "Date in UTC", example = "2017-10-20", required = true, position = 1)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private LocalDate date;

    @ApiModelProperty(value = "Start time in UTC", required = true, example = "13:59", position = 2)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = LOCAL_TIME_PATTERN)
    private LocalTime startTime;

    @ApiModelProperty(value = "End time in UTC", required = true, example = "17:59", position = 3)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    @JsonFormat(pattern = LOCAL_TIME_PATTERN)
    private LocalTime endTime;

    @ApiModelProperty(value = "Owner ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", required = true, position = 4)
    @Valid
    @NotNull
    private UUID ownerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(example = "1490134074968", position = 5)
    private Long created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(example = "1490134074968", position = 6)
    private Long lastModified;

    @ApiModelProperty(value = "Modification message", position = 7)
    private String modificationMessage;

    @ApiModelProperty(value = "Modification author ID", example = "c5296892-347f-4b2e-b1c6-6faff971f767", position = 8)
    private UUID modificationAuthorId;

    public ReservationView() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public String getModificationMessage() {
        return modificationMessage;
    }

    public void setModificationMessage(String modificationMessage) {
        this.modificationMessage = modificationMessage;
    }

    public UUID getModificationAuthorId() {
        return modificationAuthorId;
    }

    public void setModificationAuthorId(UUID modificationAuthorId) {
        this.modificationAuthorId = modificationAuthorId;
    }
}
