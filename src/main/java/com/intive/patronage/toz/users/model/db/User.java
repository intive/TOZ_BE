package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends Identifiable {
    private String name;
    private String passwordHash;
    private String surname;
    private String phoneNumber;
    private String email;

    @OneToMany(cascade = CascadeType.ALL) // TODO and cascade type
    private Set<RoleEntity> roles = new HashSet<>();

    public void addRole(final Role role) {
        final RoleEntity roleEntity = new RoleEntity(role);
        roles.add(roleEntity);
    }

    public boolean hasRole(final Role role) {
        return roles.stream().anyMatch(
                roleEntity -> roleEntity.getRole().equals(role));
    }

    public enum Role {
        SU, TOZ, VOLUNTEER
    }
}
