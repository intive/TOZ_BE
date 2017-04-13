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

    @OneToMany(cascade = CascadeType.ALL) // TODO
    private List<RoleEntity> roles = new ArrayList<>();

    public void addRole(Role role) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);
        roles.add(roleEntity);
    }

    public enum Role {
        SU, TOZ, VOLUNTEER
    }
}
