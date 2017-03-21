package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.repository.FileUploadRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private List<UploadedFile> uploadedFileListToDelete;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storageProperties = new StorageProperties();
        storageService = new FileSystemStorageService(storageProperties, fileUploadRepository);
        uploadedFileListToDelete = new ArrayList<>();
    }


    @Test
    public void saveFileTests(){

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.jpg", "text/plain", "some file".getBytes());
        UploadedFile uploadedFile = storageService.store(firstFile);
        Path path = Paths.get(storageProperties.getStoragePathRoot(),uploadedFile.getPath());
        uploadedFileListToDelete.add(uploadedFile);
        assert(Files.exists(path));
    }

    @Test
    public void saveManyFileTests(){
        UploadedFile uploadedFile;
        for (int i = 0; i < 5; i++){
            MockMultipartFile firstFile = new MockMultipartFile("data", "filename.jpg", "text/plain", "some file".getBytes());
            uploadedFile = storageService.store(firstFile);
            uploadedFileListToDelete.add(uploadedFile);
        }
    }
    @After
    public void deleteFiles(){
        for (UploadedFile uploadedFile : uploadedFileListToDelete){
            FileSystemUtils.deleteRecursively(Paths.get(storageProperties.getStoragePathRoot(),
                    uploadedFile.getPath()).toFile());
        }
    }
}
