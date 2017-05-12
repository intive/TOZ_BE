package com.intive.patronage.toz.util;

import com.intive.patronage.toz.Application;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(
        properties = {ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD}
)
public class InitRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InitRepository initRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void superAdminShouldBePresent() throws Exception {
        initRepository.initDatabase().run();

        final User admin = userRepository.findByName(InitRepository.SUPER_ADMIN_USER_NAME);
        assertThat(admin).isNotNull();
        assertThat(passwordEncoder.matches(ApiProperties.SUPER_ADMIN_PASSWORD, admin.getPasswordHash()));
    }
}
