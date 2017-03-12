package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.StorageException;
import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.repository.FileUploadRepository;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private String location = "";
    private int amountFilesInFolder;
    private FileUploadRepository fileUploadRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileUploadRepository fileUploadRepository) {
        this.amountFilesInFolder = properties.getAmountFilesInFolder();
        this.rootLocation = Paths.get(properties.getLocation());
        this.location = properties.getLocation();
        this.fileUploadRepository = fileUploadRepository;
    }

    public String getFileFolderLocation(String location) throws IOException{
        Calendar calendar = Calendar.getInstance();
        String month = new SimpleDateFormat("MM").format(calendar.getTime());
        String trueLocation =
                 File.separator
                + calendar.get(Calendar.YEAR)
                + File.separator
                + month;
        Files.createDirectories(Paths.get(location + trueLocation));
        while(Files.list(Paths.get(location + trueLocation)).count() > this.amountFilesInFolder){
            trueLocation += File.separator + "files";
            Files.createDirectories(Paths.get(location + trueLocation));
        }
        return trueLocation;
    }
    public UploadedFile saveFileUpload(String path, String fileExtension){
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setId(UUID.randomUUID());
        uploadedFile.setDate(new Date());
        fileUploadRepository.save(uploadedFile);
        path = String.format("%s%s%s.%s", path, File.separator, uploadedFile.getId(),fileExtension);
        String filename = path;
        uploadedFile.setPath(filename);
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
            String fileFolderLocation = this.getFileFolderLocation(this.location);
            uploadedFile = this.saveFileUpload(fileFolderLocation,FilenameUtils.getExtension(file.getOriginalFilename()));
            Path location = Paths.get(this.location + uploadedFile.getPath());
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
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(UUID uuid) {
        UploadedFile uploadedFile = fileUploadRepository.findOne(uuid);
        if (uploadedFile == null){
            throw new NotFoundException("Record with uuid "+uuid+" not found ");
        }
        fileUploadRepository.delete(uuid);
        Path path = Paths.get(this.location + uploadedFile.getPath());
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
