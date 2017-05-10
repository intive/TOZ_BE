package com.intive.patronage.toz.proposals;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProposalService {

    private static final String PROPOSAL = "Proposal";
    private static final String USER = "User";
    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;

    @Autowired
    ProposalService(ProposalRepository proposalRepository, UserRepository userRepository) {
        this.proposalRepository = proposalRepository;
        this.userRepository = userRepository;
    }

    List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    Proposal create(final Proposal proposal) {
        final String email = proposal.getEmail();
        if (proposalRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(PROPOSAL);
        }
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(USER);
        }
        UUID id = proposal.getId();
        return proposalRepository.save(proposal);
    }

    Proposal update(UUID id, Proposal proposal) {
        throwNotFoundExceptionIfIdNotExists(id);
        proposal.setId(id);
        return proposalRepository.save(proposal);
    }

    void delete(final UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        proposalRepository.delete(id);
    }

    void throwNotFoundExceptionIfIdNotExists(final UUID id) {
        if (!proposalRepository.exists(id)) {
            throw new NotFoundException(PROPOSAL);
        }
    }
}
