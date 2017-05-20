package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.error.exception.WrongPasswordException;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class PasswordsService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Autowired
    PasswordsService(UserService userService, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    User changePassword(String email, String newPassword) {
        final User user = userService.findOneByEmail(email);
        validateNewPassword(user, newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.update(user.getId(), user);
        return user;
    }

    void changePasswordForExistingUser(String email, String oldPassword, String newPassword) {
        final User user = userService.findOneByEmail(email);
        authenticateUser(user, oldPassword);
        validateNewPassword(user, newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.update(user.getId(), user);
    }

    private void authenticateUser(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            final String message =
                    messageSource.getMessage("wrongPassword", null, LocaleContextHolder.getLocale());

            throw new WrongPasswordException(message);
        }
    }

    private void validateNewPassword(User user, String password) {
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            final String message =
                    messageSource.getMessage("samePasswords", null, LocaleContextHolder.getLocale());

            throw new WrongPasswordException(message);
        }
    }
}
