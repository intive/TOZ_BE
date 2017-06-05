package com.intive.patronage.toz.status.model;


import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class PetStatus extends Identifiable {
    private String name;
    private String rgb;
    private boolean isPublic;
}
