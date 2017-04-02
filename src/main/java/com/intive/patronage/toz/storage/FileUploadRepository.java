package com.intive.patronage.toz.storage;

import com.intive.patronage.toz.storage.model.db.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface FileUploadRepository extends JpaRepository<UploadedFile, UUID> {
}
