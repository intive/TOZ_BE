package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Set;

public final class RolesChecker {

    private RolesChecker() {
    }

    public static boolean hasCurrentUserAdminRole() {
        return checkCurrentUserRole(Role.SA) || checkCurrentUserRole(Role.TOZ);
    }

    private static boolean checkCurrentUserRole(final Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        return grantedAuthorities.contains(role);
    }

    public static boolean hasUserSuperAdminRole(final User user) {
        Set<Role> userRoles = user.getRoles();
        return userRoles.contains(Role.SA);
    }
}
