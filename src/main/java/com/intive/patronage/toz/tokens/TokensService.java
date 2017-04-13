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

    @Autowired
    TokensService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    boolean isUserAuthenticated(String userEmail, String plainPassword) {
        User userFromDatabase = userService.findOneByEmail(userEmail);
        return passwordEncoder.matches(plainPassword, userFromDatabase.getPasswordHash());
    }
}
