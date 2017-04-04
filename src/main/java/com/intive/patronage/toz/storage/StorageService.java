package com.intive.patronage.toz.storage;

import com.intive.patronage.toz.storage.model.db.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface StorageService {

    void init();
    UploadedFile store(MultipartFile file);
    UploadedFile get(UUID uid);
    List<UploadedFile> getAll();
    void delete(UUID uuid);

}
