package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.model.db.IdentifiableView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ReservationView extends IdentifiableView {

    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;
    @NotEmpty
    @Valid
    private UserView owner;

    private LocalDateTime creationTime;
}
