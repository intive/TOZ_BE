package com.intive.patronage.toz.schedule;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScheduleControllerTest {

    private static final String SCHEDULE_PATH = "/schedule";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new ScheduleController()).build();
    }

    @Test
    public void shouldReturnBadRequestWhenParamIsMissingInGetRequest() throws Exception {
        mvc.perform(get(SCHEDULE_PATH))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenParamIsWrongTypeInGetRequest() throws Exception {
        mvc.perform(get(SCHEDULE_PATH).param("from", String.valueOf(Date.from(Instant.now()).getTime())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPostRequest() throws Exception {
        mvc.perform(post(SCHEDULE_PATH))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturnBadRequestWhenBodyIsMissingInPostRequest() throws Exception {
        mvc.perform(post(SCHEDULE_PATH)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPutRequest() throws Exception {
        mvc.perform(put(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID())))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturnBadRequestWhenBodyIsMissingInPutRequest() throws Exception {
        mvc.perform(put(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID()))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isBadRequest());
    }

}
