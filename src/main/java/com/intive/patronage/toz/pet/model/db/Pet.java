package com.intive.patronage.toz.pet.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Pet extends Identifiable {

    private String name;
    @Enumerated(value = EnumType.STRING)
    private Type type;
    @Enumerated(value = EnumType.STRING)
    private Sex sex;
    private String description;
    private String address;
    private String imageUrl;
    private UUID helperUuid;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptanceDate;

    public enum Type {
        DOG, CAT
    }

    public enum Sex {
        MALE, FEMALE
    }
}
