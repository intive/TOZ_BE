package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.db.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

public interface StorageService {

    void init();

    UploadedFile store(MultipartFile file);
    Path load(String filename);
    void delete(UUID uuid);

}
