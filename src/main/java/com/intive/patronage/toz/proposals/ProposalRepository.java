package com.intive.patronage.toz.proposals;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.proposals.model.Proposal;


public interface ProposalRepository extends IdentifiableRepository<Proposal> {

    Proposal findByEmail(String email);
    boolean existsByEmail(String email);
}
