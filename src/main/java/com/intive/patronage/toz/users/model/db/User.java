package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends PersonalData {

    private String passwordHash;

    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangeDate;

    public User(String name) {
        this.name = name;
    }

    public boolean isSuperAdmin() {
        return hasRole(Role.SA);
    }

    public Set<Role> getRoles() {
        Set<Role> userRoles = new HashSet<>();
        for (RoleEntity roleEntity : roles) {
            userRoles.add(roleEntity.getRole());
        }
        return userRoles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles.stream()
                .map(RoleEntity::buildWithRole)
                .collect(Collectors.toSet());
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
