package com.intive.patronage.toz.tokens.auth;

import com.intive.patronage.toz.users.UsersService;
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
public class SuperAdminAuthenticationProvider implements AuthenticationProvider {

    private static final String BAD_CREDENTIALS_MESSAGE = "Wrong name or password!";
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SuperAdminAuthenticationProvider(UsersService usersService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        if (!usersService.existsByName(name)) {
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }
        final User user = usersService.findOneByName(name);
        if (user.isSuperAdmin()) {
            checkPasswordMatching(authentication, user);
            final String superAdminRole = User.Role.SA.toString();
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
