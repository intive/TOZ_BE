package com.intive.patronage.toz.users;

import com.intive.patronage.toz.mail.MailService;
import com.intive.patronage.toz.mail.MailTemplatesService;
import com.intive.patronage.toz.proposals.ProposalRepository;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.tokens.JwtParser;
import com.intive.patronage.toz.users.model.db.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class UserActivationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private MailService mailService = new MailService(javaMailSender);

    @Mock
    private JwtFactory jwtFactory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProposalRepository proposalRepository;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private MailTemplatesService mailTemplatesService;

    private UserActivationService userActivationService;
    private static final String SECRET = "c2VjcmV0";
    private static final String USER_EMAIL = "email@email.com";
    private static final String USER_NAME = "username";
    private static final String USER_SURNAME = "johny";
    private static final String USER_PHONENUMBER = "123123123";
    private static final String EXAMPLE_TOPIC = "User registration";
    private static final String URL_ACTIVATION = "Localhost";
    private static final String EXAMPLE_PASSWORD = "12345";
    private static final long EXPIRATION_TIME = 100000;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, random);
        JwtParser jwtParser = new JwtParser(messageSource, SECRET);

        userActivationService = new UserActivationService(
                jwtFactory,
                userRepository,
                proposalRepository,
                jwtParser,
                mailService,
                mailTemplatesService,
                passwordEncoder
        );



    }

    @DataProvider
    public static Object[][] getProperProposalAndUser() {
        Proposal proposal = new Proposal();
        proposal.setName(USER_NAME);
        proposal.setEmail(USER_EMAIL);
        proposal.setSurname(USER_SURNAME);
        proposal.setPhoneNumber(USER_PHONENUMBER );

        User user = new User();
        user.setName(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setSurname(USER_SURNAME);
        user.setPhoneNumber(USER_PHONENUMBER );
        user.setPasswordHash(EXAMPLE_PASSWORD );

        return new Object[][]{{user, proposal}};
    }

    @Test
    @UseDataProvider("getProperProposalAndUser")
    public void checkCorrectUserActivation( final User user, final Proposal proposal) throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(proposalRepository.findByEmail(USER_EMAIL)).thenReturn(proposal);

        JwtFactory jwtFactory = new JwtFactory(SECRET );
        String token = jwtFactory.generateToken(proposal, EXPIRATION_TIME);
        User userToCompare = userActivationService.checkActivationToken(token, EXAMPLE_PASSWORD);

        assertTrue(userToCompare.getName().equals(USER_NAME));
        assertTrue(userToCompare.getEmail().equals(USER_EMAIL));
    }

}
