package com.intive.patronage.toz.news;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.news.model.db.News;
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
import org.springframework.test.context.TestPropertySource;
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
@SpringBootTest
@TestPropertySource(
        properties = {
                ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD
        }
)
public class NewsControllerImagesTest {
    private static final String IMAGES_REQUEST_PATH_FORMAT = ApiUrl.NEWS_PATH + "/%s/images";
    private static final String IMAGE_FILE_NAME = "/test.jpg";
    private static final String CORRECT_FILE_NAME = "filename.jpg";
    private static final String WRONG_FILE_NAME = "test.txt";
    private static final String WRONG_CONTENT_TYPE = "text/plain";
    private static final byte[] WRONG_CONTENT = "Spring Framework".getBytes();
    private static final String NAME = "file";
    private final News news = new News();

    private MockMvc mockMvc;
    private MockMvc mvcWithCustomHandlers;
    @Autowired
    private ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver;
    @Mock
    private StorageService storageService;
    @Mock
    private NewsService newsService;
    @Mock
    private StorageProperties storageProperties;

    @Before
    public void setUp() {
        UploadedFile uploadedFile = initializeUploadedFile();
        mockMvc = MockMvcBuilders.standaloneSetup(
                new NewsController(newsService, storageService, storageProperties)).build();
        mvcWithCustomHandlers =
                MockMvcBuilders.standaloneSetup(new NewsController(newsService, storageService,
                        storageProperties))
                        .setHandlerExceptionResolvers(exceptionHandlerExceptionResolver).build();
        when(storageService.store(any(MultipartFile.class))).thenReturn(uploadedFile);
        when(storageService.get(uploadedFile.getId())).thenReturn(uploadedFile);
        doNothing().when(newsService).updateNewsImageUrl(any(UUID.class), any(String.class));
    }

    @Test
    public void shouldReturnErrorWhenImproperMediaType() throws Exception {
        this.mockMvc.perform(post(String.format(IMAGES_REQUEST_PATH_FORMAT, news.getId())))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(storageService);
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream(IMAGE_FILE_NAME)) {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, CORRECT_FILE_NAME, MediaType.IMAGE_JPEG_VALUE,
                            inputStream);

            mockMvc.perform(fileUpload(String.format(IMAGES_REQUEST_PATH_FORMAT, news.getId()))
                    .file(multipartFile))
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
                new MockMultipartFile(NAME, WRONG_FILE_NAME, WRONG_CONTENT_TYPE, WRONG_CONTENT);
        mvcWithCustomHandlers.perform(fileUpload(String.format(IMAGES_REQUEST_PATH_FORMAT,
                news.getId())).file(multipartFile))
                .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(storageService);
    }

    private UploadedFile initializeUploadedFile() {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setPath(uploadedFile.getId().toString());
        return uploadedFile;
    }
}
