package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdentifiableRepository<T extends Identifiable> extends JpaRepository<T, UUID> {
}
