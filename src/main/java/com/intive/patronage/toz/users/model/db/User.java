package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    public enum Role implements GrantedAuthority {
        SA, TOZ, VOLUNTEER, ANONYMOUS;

        @Override
        public String getAuthority() {
            return this.toString();
        }
    }
}
