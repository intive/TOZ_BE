package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Entity
public class RoleEntity implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private PersonalData.Role role;

    private RoleEntity() {
    }

    public RoleEntity(PersonalData.Role role) {
        this.role = role;
    }

    public RoleEntity(String role) {
        this.role = PersonalData.Role.valueOf(role);
    }
}
