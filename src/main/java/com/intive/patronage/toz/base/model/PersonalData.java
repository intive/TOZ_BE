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
public class PersonalData extends Identifiable {
    private String name;
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

    public enum Role {
        SA, TOZ, VOLUNTEER, ANONYMOUS, TEMP_HOUSE
    }
}
