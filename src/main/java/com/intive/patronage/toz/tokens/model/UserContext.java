package com.intive.patronage.toz.tokens.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserContext {

    private final UUID userId;
    private final String email;
    private final Set<GrantedAuthority> authorities;
}
