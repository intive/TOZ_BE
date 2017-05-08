package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends Identifiable {
    private String name;
    private String passwordHash;
    private String surname;
    private String phoneNumber;
    @Column(unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<RoleEntity> roles = new HashSet<>();

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

    public enum Role {
        SA, TOZ, VOLUNTEER, ANONYMOUS
    }
}
