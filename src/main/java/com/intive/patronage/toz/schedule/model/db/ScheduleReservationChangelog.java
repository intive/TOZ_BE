package com.intive.patronage.toz.schedule.model.db;

import com.intive.patronage.toz.schedule.constant.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class ScheduleReservationChangelog extends Reservation {

    private UUID reservationId;
    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;
    private String modificationMessage;
    private UUID modificationAuthorUuid;
}
