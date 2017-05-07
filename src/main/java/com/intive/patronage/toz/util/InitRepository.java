package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class InitRepository {

    private static final String SUPER_ADMIN = "SA";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(@Value("${super-admin.password}") String superAdminPassword) {
        return args -> {
            final User superAdmin = new User();
            superAdmin.setName(SUPER_ADMIN);
            superAdmin.addRole(User.Role.SA);
            superAdmin.setPasswordHash(passwordEncoder.encode(superAdminPassword));
            userRepository.save(superAdmin);
        };
    }
}
