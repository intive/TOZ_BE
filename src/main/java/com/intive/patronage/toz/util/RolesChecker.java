package com.intive.patronage.toz.util;

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
        return checkCurrentUserRole(User.Role.SA) || checkCurrentUserRole(User.Role.TOZ);
    }

    private static boolean checkCurrentUserRole(final User.Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        return grantedAuthorities.contains(role);
    }

    public static boolean hasUserSuperAdminRole(final User user) {
        Set<User.Role> userRoles = user.getRoles();
        return userRoles.contains(User.Role.SA);
    }
}
