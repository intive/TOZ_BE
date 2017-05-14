package com.intive.patronage.toz.base.model;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@MappedSuperclass
public abstract class PersonalData extends Identifiable {
    protected String name;
    protected String surname;
    protected String phoneNumber;

    @Column(unique = true)
    protected String email;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Set<RoleEntity> roles = new HashSet<>();

    public void addRole(final Role role) {
        final RoleEntity roleEntity = RoleEntity.buildWithRole(role);
        roles.add(roleEntity);
    }

    public boolean hasRole(final Role role) {
        return roles.stream().anyMatch(
                roleEntity -> roleEntity.getRole().equals(role));
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
}
