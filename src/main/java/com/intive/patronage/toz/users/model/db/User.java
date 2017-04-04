package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import com.intive.patronage.toz.users.model.enumerations.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
public class User extends Identifiable {
    private String name;
    private String password;
    private String surname;
    private String phoneNumber;
    private String email; // TODO
    @Enumerated(EnumType.STRING)
    private Role role;
}
