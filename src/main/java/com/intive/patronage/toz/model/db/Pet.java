package com.intive.patronage.toz.model.db;

import com.intive.patronage.toz.model.constant.PetValues;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Pet extends Identifiable {

    private String name;
    private PetValues.Type type;
    private PetValues.Sex sex;
    private String description;
    private String address;

    @CreatedDate
    @Column(updatable = false)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    private Date lastModified;

    public Pet() {
    }

    private Pet(Builder builder) {
        if (builder.id != null) {
            this.setId(builder.id);
        }
        this.name = builder.name;
        this.type = builder.type;
        this.sex = builder.sex;
        this.description = builder.description;
        this.address = builder.address;
        this.created = builder.created;
        this.lastModified = builder.lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetValues.Type getType() {
        return type;
    }

    public void setType(PetValues.Type type) {
        this.type = type;
    }

    public PetValues.Sex getSex() {
        return sex;
    }

    public void setSex(PetValues.Sex sex) {
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

    public static class Builder {
        private UUID id;
        private String name;
        private PetValues.Type type;
        private PetValues.Sex sex;
        private String description;
        private String address;
        private Date created;
        private Date lastModified;

        public Builder() {
        }

        public Builder(UUID id) {
            this.id = id;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(PetValues.Type type) {
            this.type = type;
            return this;
        }

        public Builder setSex(PetValues.Sex sex) {
            this.sex = sex;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder setLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Pet build() {
            return new Pet(this);
        }
    }
}
