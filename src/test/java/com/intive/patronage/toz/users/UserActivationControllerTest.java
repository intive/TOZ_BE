package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.proposals.ProposalService;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Value("${jwt.secret-base64}")
    private String secret;

    @Mock
    private UserActivationService userActivationService;

    @Mock
    ProposalService proposalService;

    private MockMvc mockMvc;

    private JwtFactory jwtFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jwtFactory = new JwtFactory(secret);
        final UserActivationController petsController = new UserActivationController(userActivationService, proposalService);
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

    @Test
    public void sendTokenIsOk() throws Exception {
        final Proposal proposal = getProposalModel();
        when(proposalService.findOne(EXPECTED_ID)).thenReturn(proposal);
        final String requestUrl = String.format("%s/%s", ApiUrl.REGISTER_PATH, EXPECTED_ID);
        mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk());
        verify(userActivationService, times(1)).sendActivationEmail(any(Proposal.class));
    }
}
