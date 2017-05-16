package com.intive.patronage.toz.proposals;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.proposals.model.ProposalView;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.util.ModelMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
@SpringBootTest
public class ProposalControllerTest {
    private static final int PROPOSAL_LIST_SIZE = 3;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Jan";
    private static final String EXPECTED_SURNAME = "Kowalski";
    private static final String EXPECTED_PHONE_NUMBER = "600100200";
    private static final String EXPECTED_EMAIL = "jan.ko@gmail.com";
    private static final Role EXPECTED_ROLE = Role.VOLUNTEER;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private ProposalService proposalService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final ProposalController proposalController = new ProposalController(proposalService);
        mvc = MockMvcBuilders.standaloneSetup(proposalController).build();
    }

    @DataProvider
    public static Object[][] getProposalModelAndView() {
        final Proposal proposal = new Proposal();
        proposal.setId(EXPECTED_ID);
        proposal.setName(EXPECTED_NAME);
        proposal.setSurname(EXPECTED_SURNAME);
        proposal.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        proposal.setEmail(EXPECTED_EMAIL);
        proposal.addRole(EXPECTED_ROLE);

        final ProposalView proposalView = ModelMapper.convertToView(proposal, ProposalView.class);
        return new Object[][]{{proposal, proposalView}};
    }

    private List<Proposal> getProposals() {
        final List<Proposal> proposals = new ArrayList<>();
        for (int i = 0; i < PROPOSAL_LIST_SIZE; i++) {
            final Proposal proposal = new Proposal();
            proposal.setId(UUID.randomUUID());
            proposal.setName(String.format("%s%d", "name", i));
            proposal.setSurname(String.format("%s%d", "surname", i));
            proposal.setPhoneNumber(String.format("%s%d", "number", i));
            proposal.setEmail(String.format("%s%d", "email", i));
            proposal.addRole(Role.values()[i % 2]);
            proposals.add(proposal);
        }
        return proposals;
    }

    @Test
    public void shouldReturnProposalsList() throws Exception {
        final List<Proposal> proposals = getProposals();
        when(proposalService.findAll()).thenReturn(proposals);

        mvc.perform(get(ApiUrl.PROPOSAL_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PROPOSAL_LIST_SIZE)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].surname", notNullValue()))
                .andExpect(jsonPath("$[0].phoneNumber", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()))
                .andExpect(jsonPath("$[1].name", notNullValue()))
                .andExpect(jsonPath("$[1].surname", notNullValue()))
                .andExpect(jsonPath("$[1].phoneNumber", notNullValue()))
                .andExpect(jsonPath("$[1].email", notNullValue()))
                .andExpect(jsonPath("$[2].name", notNullValue()))
                .andExpect(jsonPath("$[2].surname", notNullValue()))
                .andExpect(jsonPath("$[2].phoneNumber", notNullValue()))
                .andExpect(jsonPath("$[2].email", notNullValue()));

        verify(proposalService, times(1)).findAll();
        verifyNoMoreInteractions(proposalService);
    }

    @Test
    @UseDataProvider("getProposalModelAndView")
    public void shouldCreateProposal(final Proposal proposal, final ProposalView proposalView) throws Exception {
        final String proposalViewJsonString = ModelMapper.convertToJsonString(proposalView);

        when(proposalService.create(any(Proposal.class))).thenReturn(proposal);
        mvc.perform(post(ApiUrl.PROPOSAL_PATH)
                .contentType(CONTENT_TYPE)
                .content(proposalViewJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)))
                .andExpect(jsonPath("$.phoneNumber", is(EXPECTED_PHONE_NUMBER)))
                .andExpect(jsonPath("$.email", is(EXPECTED_EMAIL)))
                .andExpect(jsonPath("$.roles[0]", is(EXPECTED_ROLE.toString())));

        verify(proposalService, times(1))
                .create(any(Proposal.class));
        verifyNoMoreInteractions(proposalService);
    }

    @Test
    @UseDataProvider("getProposalModelAndView")
    public void shouldUpdateProposal(final Proposal proposal, final ProposalView proposalView) throws Exception {
        final String proposalJsonString = ModelMapper.convertToJsonString(proposalView);

        when(proposalService.update(eq(EXPECTED_ID), any(Proposal.class))).thenReturn(proposal);
        mvc.perform(put(String.format("%s/%s", ApiUrl.PROPOSAL_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(proposalJsonString))
                .andExpect(status().isOk());

        verify(proposalService, times(1)).update(eq(EXPECTED_ID), any(Proposal.class));
        verifyNoMoreInteractions(proposalService);
    }

    @Test
    public void shouldDeleteProposalById() throws Exception {
        final UUID id = UUID.randomUUID();
        doNothing().when(proposalService).delete(id);
        mvc.perform(delete(String.format("%s/%s", ApiUrl.PROPOSAL_PATH, id)))
                .andExpect(status().isOk());

        verify(proposalService, times(1)).delete(id);
        verifyNoMoreInteractions(proposalService);
    }
}
