package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.constant.ApiUrl;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.service.PetsService;
import com.intive.patronage.toz.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PetsControllerImagesTest {

    private final static String IMAGES_REQUEST_PATH_FORMAT = ApiUrl.PET_PATH + "/%s/images";
    private final static String imageFileName = "/test.jpg";
    private final Pet pet = new Pet();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;
    @MockBean
    private PetsService petsService;


    @Before
    public void setUp() {
        pet.setId(UUID.randomUUID());
        UploadedFile uploadedFile = initializeUploadedFile();

        when(storageService.store(any(MultipartFile.class))).thenReturn(uploadedFile);
        when(storageService.get(uploadedFile.getId())).thenReturn(uploadedFile);
        doNothing().when(petsService).updatePetImageUrl(any(UUID.class), any(String.class));
    }

    @Test
    public void shouldReturnErrorWhenImproperMediaType() throws Exception {
        this.mockMvc.perform(post(String.format(IMAGES_REQUEST_PATH_FORMAT, pet.getId())))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(storageService);
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream(imageFileName)) {
            MockMultipartFile multipartFile =
                    new MockMultipartFile("file", "filename.jpg", MediaType.IMAGE_JPEG_VALUE, inputStream);

            mockMvc.perform(fileUpload(String.format(IMAGES_REQUEST_PATH_FORMAT, pet.getId())).file(multipartFile))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("url").isNotEmpty());

            then(storageService).should().store(multipartFile);
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void shouldReturnErrorWhenInvalidImage() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        this.mockMvc.perform(fileUpload(String.format(IMAGES_REQUEST_PATH_FORMAT, pet.getId())).file(multipartFile))
                .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(storageService);
    }

    private UploadedFile initializeUploadedFile() {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setId(UUID.randomUUID());
        uploadedFile.setPath(uploadedFile.getId().toString());
        return uploadedFile;
    }
}

