package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.DbModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ModelRepository<T extends DbModel> extends JpaRepository<T, UUID> {
}
