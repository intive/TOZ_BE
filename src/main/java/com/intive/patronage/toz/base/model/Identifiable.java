package com.intive.patronage.toz.base.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class Identifiable {

    @Id
    private UUID id = UUID.randomUUID();
}
