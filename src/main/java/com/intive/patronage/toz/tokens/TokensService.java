package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class TokensService {

    private final long expirationTime;
    private final PasswordEncoder passwordEncoder;
    private final JwtFactory jwtFactory;

    @Autowired
    TokensService(@Value("${jwt.expiration-time-minutes}") long expirationTime,
                  PasswordEncoder passwordEncoder,
                  JwtFactory jwtFactory) {
        this.expirationTime = expirationTime;
        this.passwordEncoder = passwordEncoder;
        this.jwtFactory = jwtFactory;
    }

    boolean isUserAuthenticated(final User user, final String plainPassword) {
        return passwordEncoder.matches(plainPassword, user.getPasswordHash());
    }

    String getToken(final User user) {
        return jwtFactory.generateToken(user, expirationTime);
    }
}
