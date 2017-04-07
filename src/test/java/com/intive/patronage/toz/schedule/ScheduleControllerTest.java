package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.ScheduleDataProvider.VALID_LOCAL_DATE_FROM;
import static com.intive.patronage.toz.schedule.ScheduleDataProvider.VALID_LOCAL_TIME;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
public class ScheduleControllerTest {

    private static final String SCHEDULE_PATH = "/schedule";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    private static final String VALID_LOCAL_DATE_TO = "2017-12-01";
    private static final String INVALID_LOCAL_DATE = "2017-30-01";
    private static final String INVALID_LOCAL_TIME = "30:00";

    private MockMvc mvc;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleParser scheduleParser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new ScheduleController(scheduleService, scheduleParser)).build();
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnOKWhenGetSchedule(Reservation reservation) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(scheduleService.findScheduleReservations(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(reservations);
        mvc.perform(get(SCHEDULE_PATH)
                .param("from", VALID_LOCAL_DATE_FROM)
                .param("to", VALID_LOCAL_DATE_TO))
                .andExpect(status().isOk());

        verify(scheduleService, times(1))
                .findScheduleReservations(any(LocalDate.class), any(LocalDate.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnOKWhenGetReservationById(Reservation reservation) throws Exception {
        when(scheduleService.findReservation(any(UUID.class)))
                .thenReturn(reservation);
        mvc.perform(get(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                .param("id", VALID_LOCAL_DATE_TO))
                .andExpect(status().isOk());

        verify(scheduleService, times(1)).findReservation(any(UUID.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnCreatedWhenUpdateReservation(Reservation reservation) throws Exception {
        when(scheduleService.updateReservation(any(UUID.class), any(Reservation.class)))
                .thenReturn(reservation);
        ReservationRequestView view = new ReservationRequestView();
        view.setDate(VALID_LOCAL_DATE_FROM);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setModificationMessage("string");
        view.setOwnerId(UUID.randomUUID());
        view.setEndTime(VALID_LOCAL_TIME);
        mvc.perform(put(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                .param("id", UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(view)))
                .andExpect(status().isCreated());

        verify(scheduleService, times(1))
                .updateReservation(any(UUID.class), any(Reservation.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnOKWhenDeleteReservation(Reservation reservation) throws Exception {
        when(scheduleService.removeReservation(any(UUID.class)))
                .thenReturn(reservation);
        mvc.perform(delete(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                .param("id", UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk());

        verify(scheduleService, times(1)).removeReservation(any(UUID.class));
        verifyNoMoreInteractions(scheduleService);
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
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldReturnBadRequestWhenLocalTimeIsInvalidInPost(ReservationRequestView view) throws Exception {
        view.setStartTime(INVALID_LOCAL_TIME);
        mvc.perform(post(SCHEDULE_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(view)))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldReturnBadRequestWhenLocalDateIsInvalidInPost(ReservationRequestView view) throws Exception {
        view.setDate(INVALID_LOCAL_DATE);
        mvc.perform(post(SCHEDULE_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(view)))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(scheduleService);
    }
}
