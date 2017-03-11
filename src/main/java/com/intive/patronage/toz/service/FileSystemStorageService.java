package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.StorageException;
import com.intive.patronage.toz.exception.StorageFileNotFoundException;
import com.intive.patronage.toz.model.db.FileUpload;
import com.intive.patronage.toz.repository.FileUploadRepository;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private String location = "";
    private final int amountFilesInFolder = 998;
    private FileUploadRepository fileUploadRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileUploadRepository fileUploadRepository) {

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
    public FileUpload saveFileUpload(String path, String fileExtension){
        FileUpload fileUpload = new FileUpload();
        fileUpload.setId(UUID.randomUUID());
        fileUpload.setDate(new Date());
        fileUploadRepository.save(fileUpload);
        path += File.separator + fileUpload.getId() + "." + fileExtension;
        String filename = path;
        fileUpload.setPath(filename);
        fileUploadRepository.save(fileUpload);
        return fileUpload;
    }

    @Override
    public FileUpload store(MultipartFile file) {
        FileUpload fileUpload = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String fileFolderLocation = this.getFileFolderLocation(this.location);
            fileUpload = this.saveFileUpload(fileFolderLocation,FilenameUtils.getExtension(file.getOriginalFilename()));
            Path location = Paths.get(this.location + fileUpload.getPath());
            Files.copy(file.getInputStream(), location);
            return fileUpload;
        } catch (IOException e) {
            if (fileUpload != null) {
                fileUploadRepository.delete(fileUpload.getId());
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
        FileUpload fileUpload = fileUploadRepository.findOne(uuid);
        if (fileUpload == null){
            throw new NotFoundException("Record with uuid "+uuid+" not found ");
        }
        fileUploadRepository.delete(uuid);
        Path path = Paths.get(this.location + fileUpload.getPath());
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
