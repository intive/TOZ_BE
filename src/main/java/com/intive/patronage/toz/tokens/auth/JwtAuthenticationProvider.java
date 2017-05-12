package com.intive.patronage.toz.tokens.auth;

import com.intive.patronage.toz.tokens.JwtParser;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.model.db.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtParser jwtParser;

    @Autowired
    public JwtAuthenticationProvider(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String token = (String) authentication.getCredentials();

        if (token == null) {
            List<GrantedAuthority> authorities = Collections.singletonList(Role.ANONYMOUS);
            return new JwtAuthenticationToken(null, authorities);
        }

        jwtParser.parse(token);
        final UUID userID = jwtParser.getUserId();
        final String email = jwtParser.getEmail();
        final List<String> scopes = jwtParser.getScopes();

        final Set<GrantedAuthority> authorities = scopes.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        final UserContext userContext = new UserContext(userID, email, authorities);
        return new JwtAuthenticationToken(userContext, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
