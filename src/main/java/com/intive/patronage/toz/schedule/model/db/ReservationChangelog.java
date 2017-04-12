package com.intive.patronage.toz.schedule.model.db;

import com.intive.patronage.toz.schedule.constant.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class ReservationChangelog extends BaseReservation {

    private UUID reservationId;
    private OperationType operationType;
    private String modificationMessage;
    private UUID modificationAuthorUuid;
}
