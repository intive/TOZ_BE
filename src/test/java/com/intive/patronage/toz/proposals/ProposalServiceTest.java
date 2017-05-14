package com.intive.patronage.toz.proposals;


import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.Role;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class ProposalServiceTest {
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_SURNAME = "Bravo";
    private static final String EXPECTED_MAIL = "name@server.com";
    private static final Role EXPECTED_ROLE = Role.TEMP_HOUSE;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProposalService proposalService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        proposalService = new ProposalService(proposalRepository, userRepository);
    }

    @DataProvider
    public static Object[] getProperProposal() {
        final Proposal proposal = new Proposal();
        proposal.setId(EXPECTED_ID);
        proposal.setName(EXPECTED_NAME);
        proposal.setSurname(EXPECTED_SURNAME);
        proposal.setEmail(EXPECTED_MAIL);
        proposal.addRole(EXPECTED_ROLE);
        return new Proposal[]{proposal};
    }

    @Test
    public void shouldReturnProposalsList() throws Exception {
        when(proposalRepository.findAll()).thenReturn(Collections.emptyList());

        final List<Proposal> proposals = proposalService.findAll();
        assertTrue(proposals.isEmpty());
    }
    
    
    @Test
    @UseDataProvider("getProperProposal")
    public void shouldCreateProposal(final Proposal proposal) throws Exception {
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal);

        final Proposal createdProposal = proposalService.create(proposal);
        assertEquals(EXPECTED_NAME, createdProposal.getName());
        assertEquals(EXPECTED_MAIL, createdProposal.getEmail());
        assertEquals(EXPECTED_SURNAME, createdProposal.getSurname());
        assertTrue(createdProposal.hasRole(EXPECTED_ROLE));
        verify(proposalRepository, times(1)).save(any(Proposal.class));
    }

    @Test
    public void shouldDeleteProposal() throws Exception {
        when(proposalRepository.exists(EXPECTED_ID)).thenReturn(true);
        proposalService.delete(EXPECTED_ID);

        verify(proposalRepository, times(1)).exists(eq(EXPECTED_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteProposalNotFoundException() throws Exception {
        when(proposalRepository.exists(EXPECTED_ID)).thenReturn(false);
        proposalService.delete(EXPECTED_ID);

        verify(proposalRepository, times(1)).exists(eq(EXPECTED_ID));
    }

    @Test
    @UseDataProvider("getProperProposal")
    public void updateProposal(final Proposal proposal) throws Exception {
        when(proposalRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal);
        final Proposal updatedProposal = proposalService.update(EXPECTED_ID, proposal);

        assertEquals(EXPECTED_NAME, updatedProposal.getName());
        assertEquals(EXPECTED_MAIL, updatedProposal.getEmail());
        assertEquals(EXPECTED_SURNAME, updatedProposal.getSurname());
        assertTrue(updatedProposal.hasRole(EXPECTED_ROLE));
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getProperProposal")
    public void updateProposalNotFoundException(final Proposal proposal) throws Exception {
        when(proposalRepository.exists(EXPECTED_ID)).thenReturn(false);
        proposalService.update(EXPECTED_ID, proposal);

        verify(proposalRepository, times(1)).exists(eq(EXPECTED_ID));
    }

    @Test(expected = AlreadyExistsException.class)
    @UseDataProvider("getProperProposal")
    public void createProposalWithTheSameEmail(final Proposal proposal) throws Exception {
        when(proposalRepository.existsByEmail(EXPECTED_MAIL)).thenReturn(false);
        when(userRepository.existsByEmail(EXPECTED_MAIL)).thenReturn(true);
        proposalService.create(proposal);

        verify(proposalRepository, times(1)).existsByEmail(eq(EXPECTED_MAIL));
    }

}
