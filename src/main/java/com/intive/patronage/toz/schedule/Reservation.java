package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.model.db.Identifiable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

@Entity
public class Reservation extends Identifiable {

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private UUID ownerUuid;

    @CreationTimestamp
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    private String modificationMessage;
    private UUID modificationAuthorUuid;

    public Reservation() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public String getModificationMessage() {
        return modificationMessage;
    }

    public void setModificationMessage(String modificationMessage) {
        this.modificationMessage = modificationMessage;
    }

    public UUID getModificationAuthorUuid() {
        return modificationAuthorUuid;
    }

    public void setModificationAuthorUuid(UUID modificationAuthorUuid) {
        this.modificationAuthorUuid = modificationAuthorUuid;
    }
}
