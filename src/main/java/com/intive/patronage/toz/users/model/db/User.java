package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import com.intive.patronage.toz.users.model.enumerations.Role;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class User extends Identifiable {
    private String email;
    private String password;
    private String forename;
    private String surname;
    @Enumerated(EnumType.STRING)
    private Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
