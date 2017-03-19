package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ImageUploadControllerTest {

    private final static String PATH = "/images";

    private final static String imageFileName = "/test.jpg";
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @Test
    public void shouldReturnErrorWhenImproperMediaType() throws Exception {
        this.mockMvc.perform(post(PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(storageService);
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        when(storageService.store(any(MultipartFile.class))).thenReturn(new UploadedFile());
        InputStream inputStream = this.getClass().getResourceAsStream(imageFileName);
        MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
        String response = this.mockMvc.perform(fileUpload(PATH).file(multipartFile))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertNotNull(new URL(response));
        then(this.storageService).should().store(multipartFile);
    }

    @Test
    public void shouldReturnErrorWhenInvalidImage() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        this.mockMvc.perform(fileUpload("/images").file(multipartFile))
                .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(storageService);
    }

}
