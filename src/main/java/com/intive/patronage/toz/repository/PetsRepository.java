package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.Pet;

import java.util.List;

public interface PetsRepository extends IdentifiableRepository<Pet> {
    List<Pet> findByNameNotNullAndTypeNotNullAndSexNotNull();
}
