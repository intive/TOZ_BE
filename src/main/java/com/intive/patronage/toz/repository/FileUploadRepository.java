package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileUploadRepository extends JpaRepository<UploadedFile, UUID> {
}
