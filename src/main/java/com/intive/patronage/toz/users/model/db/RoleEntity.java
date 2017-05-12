package com.intive.patronage.toz.users.model.db;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Entity
class RoleEntity implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private User.Role role;

    private RoleEntity() {
    }

    private RoleEntity(String role) {
        this.role = User.Role.valueOf(role);
    }

    static RoleEntity buildWithRole(User.Role role) {
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
