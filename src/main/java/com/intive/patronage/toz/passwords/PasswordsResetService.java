package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.mail.MailService;
import com.intive.patronage.toz.mail.MailTemplatesService;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.tokens.JwtParser;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class PasswordsResetService {

    private final PasswordsService passwordService;
    private final JwtParser jwtParser;
    private final JwtFactory jwtFactory;
    private final MailTemplatesService mailTemplatesService;
    private final MailService mailService;

    private long expirationTime;
    private String mailSubject;

    @Autowired
    PasswordsResetService(
            PasswordsService passwordService,
            JwtParser jwtParser,
            JwtFactory jwtFactory,
            MailTemplatesService mailTemplatesService,
            MailService mailService,
            @Value("${mail.reset-password.subject}") String mailSubject,
            @Value("${jwt.email.activation.expiration-time-minutes}") long expirationTime
    ){
        this.passwordService = passwordService;
        this.jwtParser = jwtParser;
        this.jwtFactory = jwtFactory;
        this.mailTemplatesService = mailTemplatesService;
        this.mailService = mailService;
        this.mailSubject = mailSubject;
        this.expirationTime = expirationTime;
    }

    User changePasswordUsingToken(String token, String newPassword) {

        jwtParser.parse(token);
        String userEmail = jwtParser.getEmail();
        return passwordService.changePassword(userEmail, newPassword);
    }

    void sendResetPaswordToken(User user) throws IOException, MessagingException {

        String token = jwtFactory.generateToken(user, expirationTime);
        String mailBody = mailTemplatesService.getResetPasswordTemplate(token);
        mailService.sendMail(mailSubject, mailBody, user.getEmail());
    }
}
