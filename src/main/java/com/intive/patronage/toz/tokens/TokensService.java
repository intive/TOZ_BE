package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TokensService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TokensService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    boolean isUserAuthenticated(String plainPassword, String userEmail) {
        User userFromDatabase = userService.findOneByEmail(userEmail);
        // TODO remove this line and uncomment line below when hashes of pass are stored in db
        return plainPassword.matches(userFromDatabase.getPassword());
        //return passwordEncoder.matches(plainPassword, userFromDatabase.getPassword());
    }

    User getUserByMail(String userEmail) {
        return userService.findOneByEmail(userEmail);
    }
}
