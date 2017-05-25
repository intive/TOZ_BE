package com.intive.patronage.toz.status;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.status.model.PetsStatus;

public interface PetsStatusRepository extends IdentifiableRepository<PetsStatus> {

    boolean existsStatusByName(String email);
}
