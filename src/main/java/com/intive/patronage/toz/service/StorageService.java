package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.db.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface StorageService {

    void init();
    UploadedFile store(MultipartFile file);
    UploadedFile get(UUID uid);
    List<UploadedFile> getAll();
    void delete(UUID uuid);

}
