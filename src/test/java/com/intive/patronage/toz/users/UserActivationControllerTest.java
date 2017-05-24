package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.proposals.ProposalService;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserActivationRequestView;
import com.intive.patronage.toz.util.ModelMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(
        properties = {
                ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD
        }
)
public class UserActivationControllerTest {

    private static final long EXPIRATION_TIME = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Jan";
    private static final String EXPECTED_SURNAME = "Kowalski";
    private static final String EXPECTED_PHONE_NUMBER = "600100200";
    private static final String EXPECTED_EMAIL = "jan.ko@gmail.com";
    private static final Role EXPECTED_ROLE = Role.VOLUNTEER;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Value("${jwt.secret-base64}")
    private String secret;

    @Mock
    private UserActivationService userActivationService;

    @Mock
    ProposalService proposalService;

    @MockBean
    private MessageSource messageSource;

    private MockMvc mockMvc;

    private JwtFactory jwtFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jwtFactory = new JwtFactory(secret);
        final UserActivationController petsController = new UserActivationController(userActivationService, proposalService, messageSource);
        mockMvc = MockMvcBuilders.standaloneSetup(petsController).build();
    }

    private Proposal getProposalModel(){
        final Proposal proposal = new Proposal();
        proposal.setId(EXPECTED_ID);
        proposal.setName(EXPECTED_NAME);
        proposal.setSurname(EXPECTED_SURNAME);
        proposal.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        proposal.setEmail(EXPECTED_EMAIL);
        proposal.addRole(EXPECTED_ROLE);

        return proposal;
    }

    private User getUserModel(){
        final User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setSurname(EXPECTED_SURNAME);
        user.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        user.setEmail(EXPECTED_EMAIL);
        user.addRole(EXPECTED_ROLE);

        return user;
    }

    @Test
    public void sendTokenIsOk() throws Exception {
        final Proposal proposal = getProposalModel();
        when(proposalService.findOne(EXPECTED_ID)).thenReturn(proposal);

        UserActivationRequestView userActivationRequestView = new UserActivationRequestView();
        userActivationRequestView.setUuid(EXPECTED_ID);
        final String UserActivationRequestViewJsonString = ModelMapper.convertToJsonString(userActivationRequestView);

        final String requestUrl = String.format("%s", ApiUrl.REGISTER_PATH);
        mockMvc.perform(post(requestUrl)
                .contentType(CONTENT_TYPE)
                .content(UserActivationRequestViewJsonString))
                .andExpect(status().isOk());
        verify(userActivationService, times(1)).sendActivationEmailIfProposalExists(any(UUID.class));
    }
}
