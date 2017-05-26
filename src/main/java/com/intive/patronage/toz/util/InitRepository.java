package com.intive.patronage.toz.util;

import com.intive.patronage.toz.status.PetsStatusRepository;
import com.intive.patronage.toz.status.model.PetsStatus;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class InitRepository {

    static final String SUPER_ADMIN_USER_NAME = "admin";

    private static final Logger LOG = LoggerFactory.getLogger(InitRepository.class);
    private static final String MISSING_CONFIGURATION_MSG = "Missing super admin password, set env: TOZ_BE_SA_PASSWORD";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PetsStatusRepository petsStatusRepository;

    @Value("${super-admin.email:maciej.lotysz@intive.com}")
    private String email;

    @Value("${super-admin.password}")
    private String password;

    @Autowired
    public InitRepository(UserRepository userRepository, PasswordEncoder passwordEncoder, PetsStatusRepository petsStatusRepository) {
        this.userRepository = userRepository;
        this.petsStatusRepository = petsStatusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // TODO move to UserRepository?
            if (!userRepository.existsByName(SUPER_ADMIN_USER_NAME)) {
                final User admin = new User();
                admin.setName(SUPER_ADMIN_USER_NAME);
                admin.setEmail(email);
                admin.setPasswordHash(passwordEncoder.encode(Objects.requireNonNull(password, MISSING_CONFIGURATION_MSG)));
                admin.addRole(Role.SA);
                userRepository.save(admin);
                LOG.info("Created super admin: " + admin);
            }


            PetsStatus petsStatus = new PetsStatus();
            petsStatus.setName("ODEBRANY PRZEZ WŁAŚCICIELA");
            petsStatus.setRgb("#0000FF");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("WYPUSZCZONY W MIEJSCE BYTOWANIA");
            petsStatus.setRgb("#007FFF");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("EUTANAZJA");
            petsStatus.setRgb("#FF0000");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("ZGON");
            petsStatus.setRgb("#FF0000");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("PRZEKAZANY");
            petsStatus.setRgb("#FFFF00");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("USUNIĘTY");
            petsStatus.setRgb("#606060");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("ADOPTOWANY");
            petsStatus.setRgb("#008000");
            petsStatus.setPublic(false);
            petsStatusRepository.save(petsStatus);
            petsStatus = new PetsStatus();
            petsStatus.setName("DO ADOPCJI");
            petsStatus.setRgb("#008000");
            petsStatus.setPublic(true);
            petsStatusRepository.save(petsStatus);
            LOG.info("Created predefined pets statuses");

        };
    }
}
