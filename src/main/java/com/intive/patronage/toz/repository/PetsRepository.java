package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetsRepository extends JpaRepository<Pet, Long> {
    Pet findByName(String name);
}
