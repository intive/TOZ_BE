package com.intive.patronage.toz.news.model.db;

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
public class News extends Identifiable {
    private String title;
    private String contents;
    private Type type;
    private String photoUrl;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date published;

    public enum Type {
        RELEASED, UNRELEASED, ARCHIVED
    }
}
