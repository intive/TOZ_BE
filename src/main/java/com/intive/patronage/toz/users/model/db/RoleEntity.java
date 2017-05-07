package com.intive.patronage.toz.users.model.db;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Entity
public class RoleEntity implements GrantedAuthority, Serializable { // TODO change name

    @Id
    @Enumerated(EnumType.STRING)
    private User.Role role;

    private RoleEntity() {
    }

    static RoleEntity buildWithRole(User.Role role) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.role = role;
        return roleEntity;
    }

    @Override
    public String getAuthority() { // TODO delete
        return role.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        RoleEntity roleEntity = (RoleEntity) obj;
        return roleEntity.getRole().equals(role);
    }
}
