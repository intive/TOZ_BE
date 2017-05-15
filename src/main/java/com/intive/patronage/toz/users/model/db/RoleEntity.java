package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "role")
public class RoleEntity extends Identifiable implements Serializable {

    @Enumerated(EnumType.STRING)
    private Role role;

    private RoleEntity() {
    }

    private RoleEntity(String role) {
        this.role = Role.valueOf(role);
    }

    public static RoleEntity buildWithRole(Role role) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.role = role;
        return roleEntity;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "role=" + role +
                '}';
    }
}
