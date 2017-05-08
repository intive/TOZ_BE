package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class TokensService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFactory jwtFactory;

    @Autowired
    TokensService(UserService userService, PasswordEncoder passwordEncoder, JwtFactory jwtFactory) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtFactory = jwtFactory;
    }

    boolean isUserAuthenticated(String userEmail, String plainPassword) {
        User user = userService.findOneByEmail(userEmail);
        return passwordEncoder.matches(plainPassword, user.getPasswordHash());
    }

    String getToken(String userEmail) {
        return jwtFactory.generateToken(userService.findOneByEmail(userEmail));
    }

    User getUser(String userEmail) {
        return userService.findOneByEmail(userEmail);
    }
}
