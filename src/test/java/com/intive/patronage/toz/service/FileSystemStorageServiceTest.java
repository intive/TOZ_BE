package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.repository.FileUploadRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class FileSystemStorageServiceTest {

    @Mock
    private FileUploadRepository fileUploadRepository;

    private StorageProperties storageProperties;

    private StorageService storageService;

    @Test
    public void saveFileTests(){
        storageProperties = new StorageProperties();
        this.storageService = new FileSystemStorageService(storageProperties, fileUploadRepository);
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.jpg", "text/plain", "some file".getBytes());
        UploadedFile uploadedFile = storageService.store(firstFile);
        Path path = Paths.get(storageProperties.getLocation() + uploadedFile.getPath());
        assert(Files.exists(path));
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Test
    public void saveManyFileTests(){
        UploadedFile uploadedFile = null;
        List<UploadedFile> uploadedFileList = new ArrayList<UploadedFile>();
        for (int i = 0; i < 2000; i++){
            storageProperties = new StorageProperties();
            this.storageService = new FileSystemStorageService(storageProperties, fileUploadRepository);
            MockMultipartFile firstFile = new MockMultipartFile("data", "filename.jpg", "text/plain", "some file".getBytes());
            uploadedFile = storageService.store(firstFile);
            uploadedFileList.add(uploadedFile);
        }
        assert(uploadedFile.getPath().contains("\\files"));
        for (UploadedFile fu : uploadedFileList){
            Path path = Paths.get(storageProperties.getLocation() + fu.getPath());
            FileSystemUtils.deleteRecursively(path.toFile());
        }
    }
}
