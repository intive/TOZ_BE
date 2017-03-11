package com.intive.patronage.toz.model.db;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class DbModel {

    @Id
    private UUID id = UUID.randomUUID();

    public UUID getId() {
        return id;
    }

    void setId(UUID id) {
        this.id = id;
    }
}
