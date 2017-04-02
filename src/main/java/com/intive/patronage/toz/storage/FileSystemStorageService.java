package com.intive.patronage.toz.storage;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.error.exception.StorageException;
import com.intive.patronage.toz.storage.model.db.UploadedFile;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private String location = "";
    private int amountFilesInFolder;
    private FileUploadRepository fileUploadRepository;

    @Autowired
    FileSystemStorageService(StorageProperties properties, FileUploadRepository fileUploadRepository) {
        this.amountFilesInFolder = properties.getMaxAmountFilesInDir();
        this.rootLocation = Paths.get(properties.getStoragePathRoot());
        this.location = properties.getStoragePathRoot();
        this.fileUploadRepository = fileUploadRepository;
    }

    private UploadedFile saveFileUpload(String fileExtension) throws IOException {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setId(UUID.randomUUID());
        uploadedFile.setCreateDate(new Date());
        fileUploadRepository.save(uploadedFile);

        String[] folder = uploadedFile.getId().toString().split("-");

        Iterable<String> result = Splitter.fixedLength(2).split(folder[0]);
        String[] parts = Iterables.toArray(result, String.class);

        Path storageLocationPath = Paths.get(parts[0], parts[1], parts[2]);
        Path storageLocation = Paths.get(
                storageLocationPath.toString(),
                String.format("%s.%s", uploadedFile.getId(), fileExtension));
        Files.createDirectories(Paths.get(location, storageLocationPath.toString()));

        uploadedFile.setPath(storageLocation.toString());
        fileUploadRepository.save(uploadedFile);
        return uploadedFile;
    }

    @Override
    public UploadedFile store(MultipartFile file) {
        UploadedFile uploadedFile = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            uploadedFile = this.saveFileUpload(FilenameUtils.getExtension(file.getOriginalFilename()));
            Path location = Paths.get(this.location, uploadedFile.getPath());
            Files.copy(file.getInputStream(), location);
            return uploadedFile;
        } catch (IOException e) {
            if (uploadedFile != null) {
                fileUploadRepository.delete(uploadedFile.getId());
            }
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public UploadedFile get(UUID uuid) {
        return fileUploadRepository.findOne(uuid);
    }

    @Override
    public List<UploadedFile> getAll() {
        return fileUploadRepository.findAll();
    }

    @Override
    public void delete(UUID uuid) {
        UploadedFile uploadedFile = fileUploadRepository.findOne(uuid);
        if (uploadedFile == null) {
            throw new NotFoundException("Record with uuid " + uuid + " not found ");
        }
        fileUploadRepository.delete(uuid);
        Path path = Paths.get(this.location, uploadedFile.getPath());
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
