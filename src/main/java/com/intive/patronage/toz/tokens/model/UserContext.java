package com.intive.patronage.toz.tokens.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

public class UserContext {

    private final UUID userId;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    public UserContext(UUID userId, String email, Set<GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
