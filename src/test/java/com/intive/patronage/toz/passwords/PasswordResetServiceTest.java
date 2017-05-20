package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.mail.MailService;
import com.intive.patronage.toz.mail.MailTemplatesService;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.tokens.JwtParser;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PasswordResetServiceTest {

    private static final String EMAIL = "user@mail.com";
    private static final String OLD_PASSWORD = "OldPassword";
    private static final String NEW_PASSWORD = "NewPassword";
    private static final String SECRET = "abcxyz";
    private static final String PASSWORD_RESET_TOPIC = "TOZ RESET PASSWORD";
    private static final String TEST_MESSAGE_CONTENT = "TEST MESSAGE";
    private static final long EXPIRATION_TIME = 5;

    private PasswordsResetService passwordsResetService;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailService mailService = new MailService(javaMailSender);

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private MailTemplatesService mailTemplatesService;

    private PasswordsService passwordsService;
    private User user;
    private String testToken;

    @Before
    public void setUp() throws Exception {
        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, random);
        JwtParser jwtParser = new JwtParser(messageSource, SECRET);
        JwtFactory jwtFactory = new JwtFactory(SECRET);
        PasswordsService passwordsService = new PasswordsService(userService, passwordEncoder, messageSource);

        passwordsResetService = new PasswordsResetService(
                passwordsService,
                jwtParser,
                jwtFactory,
                mailTemplatesService,
                mailService,
                PASSWORD_RESET_TOPIC,
                EXPIRATION_TIME
        );

        user = new User();
        user.setPasswordHash(passwordEncoder.encode(OLD_PASSWORD));
        user.setEmail(EMAIL);

        testToken = createTestToken();
    }
    @Test
    public void shouldResetPassword() throws Exception {

        when(userService.findOneByEmail(EMAIL)).thenReturn(user);
        passwordsResetService.changePasswordUsingToken(testToken, NEW_PASSWORD);
        verify(userService).update(user.getId(), user);
    }
    @Test
    public void shouldSendResetPasswordToken() throws Exception {
        when(userService.findOneByEmail(EMAIL)).thenReturn(user);
        when(mailTemplatesService.getRegistrationTemplate(any(String.class))).thenReturn(TEST_MESSAGE_CONTENT);
        Session session = null;
        MimeMessage mimeMessage = new MimeMessage(session);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        passwordsResetService.sendResetPaswordToken(user);
        verify(mailTemplatesService).getRegistrationTemplate(testToken);
        verify(javaMailSender).send(mimeMessage);
    }

    public String createTestToken(){
        JwtFactory jwtFactory = new JwtFactory(SECRET);
        return jwtFactory.generateToken(user, EXPIRATION_TIME);
    }
}
