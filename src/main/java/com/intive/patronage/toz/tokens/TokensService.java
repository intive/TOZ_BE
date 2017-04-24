package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UsersService;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class TokensService {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFactory jwtFactory;

    @Autowired
    TokensService(UsersService usersService, PasswordEncoder passwordEncoder, JwtFactory jwtFactory) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.jwtFactory = jwtFactory;
    }

    boolean isUserAuthenticated(String userEmail, String plainPassword) {
        User userFromDatabase = usersService.findOneByEmail(userEmail);
        return passwordEncoder.matches(plainPassword, userFromDatabase.getPasswordHash());
    }

    String getToken(String userEmail) {
        return jwtFactory.generateToken(usersService.findOneByEmail(userEmail));
    }
}
