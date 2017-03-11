package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.db.FileUpload;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    FileUpload store(MultipartFile file);
    Path load(String filename);
    void delete(UUID uuid);

}
