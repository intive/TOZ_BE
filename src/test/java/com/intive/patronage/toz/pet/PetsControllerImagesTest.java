package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.storage.StorageProperties;
import com.intive.patronage.toz.storage.StorageService;
import com.intive.patronage.toz.storage.model.db.UploadedFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.io.InputStream;
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jwt.secret-base64=c2VjcmV0"
)
public class PetsControllerImagesTest {

    private final static String IMAGES_REQUEST_PATH_FORMAT = ApiUrl.PETS_PATH + "/%s/images";
    private final static String IMAGE_FILE_NAME = "/test.jpg";
    private final Pet pet = new Pet();

    private MockMvc mockMvc;
    private MockMvc mvcWithCustomHandlers;
    @Autowired
    private ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver;
    @Mock
    private StorageService storageService;
    @Mock
    private PetsService petsService;
    @Mock
    private StorageProperties storageProperties;

    @Before
    public void setUp() {
        UploadedFile uploadedFile = initializeUploadedFile();
        mockMvc = MockMvcBuilders.standaloneSetup(
                new PetsController(petsService, storageService, storageProperties)).build();
        mvcWithCustomHandlers =
                MockMvcBuilders.standaloneSetup(new PetsController(petsService, storageService, storageProperties))
                        .setHandlerExceptionResolvers(exceptionHandlerExceptionResolver).build();
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
        try (InputStream inputStream = getClass().getResourceAsStream(IMAGE_FILE_NAME)) {
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
        mvcWithCustomHandlers.perform(fileUpload(String.format(IMAGES_REQUEST_PATH_FORMAT, pet.getId())).file(multipartFile))
                .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(storageService);
    }

    private UploadedFile initializeUploadedFile() {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setPath(uploadedFile.getId().toString());
        return uploadedFile;
    }
}


