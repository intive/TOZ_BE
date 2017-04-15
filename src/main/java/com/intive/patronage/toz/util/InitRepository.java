package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
class InitRepository {

    private static final String SUPER_ADMIN = "SU";

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            final User superAdmin = new User();
            superAdmin.setName(SUPER_ADMIN);
            superAdmin.setRoles(Collections.singleton(new RoleEntity(User.Role.SA.toString())));
        };
    }
}
