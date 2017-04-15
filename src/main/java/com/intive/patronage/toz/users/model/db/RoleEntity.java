package com.intive.patronage.toz.users.model.db;

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
    private User.Role role;

    private RoleEntity() {
    }

    public RoleEntity(User.Role role) {
        this.role = role;
    }

    public RoleEntity(String role) {
        this.role = User.Role.valueOf(role);
    }
}
