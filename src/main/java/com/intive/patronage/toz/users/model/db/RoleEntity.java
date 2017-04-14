package com.intive.patronage.toz.users.model.db;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Entity
public class RoleEntity implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private User.Role role;

    private RoleEntity() {
    }

    static RoleEntity buildWithRole(@NotNull User.Role role) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.role = role;
        return roleEntity;
    }
}
