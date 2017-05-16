package com.intive.patronage.toz.proposals.model;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Proposal extends PersonalData {

    @CreationTimestamp
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    boolean isRead;

    @Override
    public String toString() {
        return "Proposal{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", creationDate=" + creationDate +
                ", isRead=" + isRead +
                '}';
    }
}
