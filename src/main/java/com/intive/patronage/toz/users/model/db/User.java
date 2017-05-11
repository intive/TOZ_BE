package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

    public User(String name) {
        this.name = name;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        passwordChangeDate = new Date();
    }

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

    public Set<User.Role> getRoles() {
        Set<User.Role> userRoles = new HashSet<>();
        for (RoleEntity roleEntity : roles) {
            userRoles.add(roleEntity.getRole());
        }
        return userRoles;
    }

    public void setRoles(Set<User.Role> roles) {
        this.roles = roles.stream()
                .map(RoleEntity::buildWithRole)
                .collect(Collectors.toSet());
    }

    public enum Role implements GrantedAuthority {
        SA, TOZ, VOLUNTEER, ANONYMOUS;

        @Override
        public String getAuthority() {
            return this.toString();
        }
    }
}
