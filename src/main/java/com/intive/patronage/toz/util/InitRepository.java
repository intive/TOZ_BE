package com.intive.patronage.toz.util;

import com.intive.patronage.toz.status.PetsStatusRepository;
import com.intive.patronage.toz.status.model.PetStatus;
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


            PetStatus petStatus = new PetStatus();
            petStatus.setName("ODEBRANY PRZEZ WŁAŚCICIELA");
            petStatus.setRgb("#0000FF");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("WYPUSZCZONY W MIEJSCE BYTOWANIA");
            petStatus.setRgb("#007FFF");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("EUTANAZJA");
            petStatus.setRgb("#FF0000");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("ZGON");
            petStatus.setRgb("#FF0000");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("PRZEKAZANY");
            petStatus.setRgb("#FFFF00");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("USUNIĘTY");
            petStatus.setRgb("#606060");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("ADOPTOWANY");
            petStatus.setRgb("#008000");
            petStatus.setPublic(false);
            petsStatusRepository.save(petStatus);
            petStatus = new PetStatus();
            petStatus.setName("DO ADOPCJI");
            petStatus.setRgb("#008000");
            petStatus.setPublic(true);
            petsStatusRepository.save(petStatus);
            LOG.info("Created predefined pets statuses");

        };
    }
}
