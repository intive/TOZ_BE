package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
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

    boolean login(UserView userView) {
        User user = userService.findOneByEmail(userView.getEmail());
        return passwordEncoder.matches(userView.getPassword(), user.getPassword());
    }
}
