package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.model.db.Identifiable;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends Identifiable{

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private UUID ownerUuid;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @LastModifiedDate
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    private String modificationMessage;
    private UUID modificationAuthorUuid;
}
