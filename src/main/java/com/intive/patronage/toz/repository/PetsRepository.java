package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.Pet;

public interface PetsRepository extends ModelRepository<Pet> {
    Pet findByName(String name);
}
