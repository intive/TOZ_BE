package com.intive.patronage.toz.users.model.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
class RoleEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private User.Role role;

    private RoleEntity() {
    }

    private RoleEntity(User.Role role) {
        this.role = role;
    }

    static RoleEntity buildWithRole(@NotNull User.Role role) {
        return new RoleEntity(role);
    }
}
