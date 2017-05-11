package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class TokensService {

    private final PasswordEncoder passwordEncoder;
    private final JwtFactory jwtFactory;

    @Autowired
    TokensService(PasswordEncoder passwordEncoder, JwtFactory jwtFactory) {
        this.passwordEncoder = passwordEncoder;
        this.jwtFactory = jwtFactory;
    }

    boolean isUserAuthenticated(User user, String plainPassword) {
        return passwordEncoder.matches(plainPassword, user.getPasswordHash());
    }

    String getToken(User user) {
        return jwtFactory.generateToken(user);
    }
}
