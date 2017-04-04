package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.pet.model.db.Pet;

import java.util.List;

public interface PetsRepository extends IdentifiableRepository<Pet> {
    List<Pet> findByNameNotNullAndTypeNotNullAndSexNotNull();
}
