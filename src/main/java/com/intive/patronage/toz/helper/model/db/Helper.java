package com.intive.patronage.toz.helper.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Helper extends Identifiable {
    private String name;
    private String surname;
    private String notes;
    private String phoneNumber;
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Helper.Category category;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    public enum Category {
        GUARDIAN, TEMPORARY_HOUSE_CAT, TEMPORARY_HOUSE_DOG, TEMPORARY_HOUSE_OTHER
    }
}
