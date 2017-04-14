package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends Identifiable {
    private String name;
    private String passwordHash;
    private String surname;
    private String phoneNumber;
    private String email;

    @OneToMany(cascade = CascadeType.ALL) // TODO change to set and cascade type
    private List<RoleEntity> roles = new ArrayList<>();

    public void addRole(final Role role) {
        roles.add(RoleEntity.buildWithRole(role));
    }

    public boolean hasRole(final Role role) {
        return roles.stream().anyMatch(
                roleEntity -> roleEntity.getRole().equals(role));
    }

    public enum Role {
        SU, TOZ, VOLUNTEER
    }
}
