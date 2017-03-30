package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.service.ScheduleService;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
public class ScheduleControllerTest {

    private static final String SCHEDULE_PATH = "/schedule";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    private static final String VALID_LOCAL_DATE = "2017-03-01";
    private static final String VALID_LOCAL_TIME = "10:00";
    private static final String INVALID_LOCAL_DATE = "2017-30-01";
    private static final String INVALID_LOCAL_TIME = "30:00";

    private MockMvc mvc;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleParser scheduleParser;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new ScheduleController(scheduleService, scheduleParser)).build();
    }

    @DataProvider
    public static Object[] getReservationRequestView() {
        ReservationRequestView view = new ReservationRequestView();
        view.setDate(VALID_LOCAL_DATE);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setEndTime(VALID_LOCAL_TIME);
        view.setOwnerId(UUID.randomUUID());
        view.setModificationAuthorId(UUID.randomUUID());
        view.setModificationMessage("string");
        return new ReservationRequestView[]{view};
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

    @Test
    @UseDataProvider("getReservationRequestView")
    public void shouldReturnBadRequestWhenLocalTimeIsInvalidInPost(ReservationRequestView view) throws Exception {
        view.setStartTime(INVALID_LOCAL_TIME);
        mvc.perform(post(SCHEDULE_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(view)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UseDataProvider("getReservationRequestView")
    public void shouldReturnBadRequestWhenLocalDateIsInvalidInPost(ReservationRequestView view) throws Exception {
        view.setDate(INVALID_LOCAL_DATE);
        mvc.perform(post(SCHEDULE_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(view)))
                .andExpect(status().isBadRequest());
    }

}
