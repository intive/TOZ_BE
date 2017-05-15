package com.intive.patronage.toz.users.model.db;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SA, TOZ, VOLUNTEER, ANONYMOUS;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
