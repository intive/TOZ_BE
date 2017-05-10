package com.intive.patronage.toz.base.model;

import com.intive.patronage.toz.users.model.db.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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
    protected Set<RoleEntity> roles = new HashSet<>();

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

    public enum Role {
        SA, TOZ, VOLUNTEER, ANONYMOUS, TEMP_HOUSE
    }
}
