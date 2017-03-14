package com.intive.patronage.toz.model.db;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Pet extends Identifiable {

    private static final String DATE_PATTERN = "dd-MM-yy HH:mm:ss";
    private String name;
    private Type type;
    private Sex sex;
    private String description;
    private String address;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = DATE_PATTERN)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonFormat(pattern = DATE_PATTERN)
    private Date lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public enum Type {
        DOG, CAT, UNKNOWN;

        @JsonCreator
        public static Type fromString(String key) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Sex {
        MALE, FEMALE;

        @JsonCreator
        public static Sex fromString(String key) {
            for (Sex sex : values()) {
                if (sex.name().equalsIgnoreCase(key)) {
                    return sex;
                }
            }
            return null;
        }
    }
}
