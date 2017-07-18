package com.intive.patronage.toz.status;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.status.model.PetStatus;

public interface PetsStatusRepository extends IdentifiableRepository<PetStatus> {

    boolean existsStatusByName(String email);
}
