package com.intive.patronage.toz.tokens.auth;

import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
class SuperAdminAuthenticationProvider implements AuthenticationProvider {

    private static final String BAD_CREDENTIALS_MESSAGE = "Wrong name or password!";
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    SuperAdminAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        if (!userService.existsByName(name)) {
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }
        final User user = userService.findOneByName(name);
        if (user.isSuperAdmin()) {
            checkPasswordMatching(authentication, user);
            final String superAdminRole = Role.SA.toString();
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singleton(new SimpleGrantedAuthority(superAdminRole));
            return new UsernamePasswordAuthenticationToken(name, authentication.getCredentials(), authorities);
        }
        throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
    }

    private void checkPasswordMatching(Authentication authentication, User user) {
        String password = (String) authentication.getCredentials();
        boolean isPasswordMatch = passwordEncoder.matches(password, user.getPasswordHash());
        if (!isPasswordMatch) {
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
