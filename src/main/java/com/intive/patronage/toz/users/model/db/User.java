package com.intive.patronage.toz.users.model.db;

import com.google.common.collect.Lists;
import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends Identifiable {
    private String name;
    private String passwordHash;
    private String surname;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangeDate;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<RoleEntity> roles = new HashSet<>();

    public User(String name, String passwordHash, String surname,
                String phoneNumber, String email, Set<RoleEntity> roles) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roles = roles;
    }

    public void addRole(final Role role) {
        final RoleEntity roleEntity = new RoleEntity(role);
        roles.add(roleEntity);
    }

    public boolean hasRole(final Role role) {
        return roles.stream().anyMatch(
                roleEntity -> roleEntity.getRole().equals(role));
    }

    public boolean isSuperAdmin() {
        return hasRole(Role.SA);
    }

    public List<Role> getRolesList() {
        return roles.stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        passwordChangeDate = new Date();
    }

    public enum Role {
        SA, TOZ, VOLUNTEER, ANONYMOUS
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", passwordChangeDate=" + passwordChangeDate +
                ", roles=" + roles +
                '}';
    }
}
