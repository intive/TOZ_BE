package com.intive.patronage.toz.tokens.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public class UserContext {

    private final UUID userId;
    private final String email;
    private final List<GrantedAuthority> authorities;

    public UserContext(UUID userId, String email, List<GrantedAuthority> authorities) {
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

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
