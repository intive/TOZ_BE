package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.error.exception.WrongPasswordException;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PasswordsServiceTest {

    private static final String EMAIL = "user@mail.com";
    private static final String OLD_PASSWORD = "OldPassword";
    private static final String NEW_PASSWORD = "NewPassword";

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;

    private PasswordsService passwordsService;
    private User user;

    @Before
    public void setUp() throws Exception {
        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, random);

        passwordsService = new PasswordsService(userService, passwordEncoder, messageSource);

        user = new User();
        user.setPasswordHash(passwordEncoder.encode(OLD_PASSWORD));
        user.setEmail(EMAIL);
    }

    @Test(expected = WrongPasswordException.class)
    public void shouldThrowExceptionWhenPasswordsDoNotMatch() throws Exception {
        when(userService.findOneByEmail(EMAIL)).thenReturn(user);
        passwordsService.changePassword(EMAIL, NEW_PASSWORD, NEW_PASSWORD);
    }

    @Test(expected = WrongPasswordException.class)
    public void shouldThrowExceptionWhenNewAndOldPasswordsAreTheSame() throws Exception {
        when(userService.findOneByEmail(EMAIL)).thenReturn(user);
        passwordsService.changePassword(EMAIL, OLD_PASSWORD, OLD_PASSWORD);
    }

    @Test
    public void shouldChangePassword() throws Exception {
        when(userService.findOneByEmail(EMAIL)).thenReturn(user);
        passwordsService.changePassword(EMAIL, OLD_PASSWORD, NEW_PASSWORD);
        verify(userService).update(user.getId(), user);
    }
}
