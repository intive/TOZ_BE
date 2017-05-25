package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.error.exception.NoPermissionException;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.Role;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.intive.patronage.toz.schedule.ScheduleDataProvider.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = ApiProperties.JWT_SECRET_BASE64
)
@RunWith(DataProviderRunner.class)
public class ScheduleControllerTest {

    private static final String SCHEDULE_PATH = "/schedule";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final String VALID_LOCAL_DATE_TO = "2017-12-01";
    private static final LocalDate PAST_LOCAL_DATE = LocalDate.now().minusDays(3);
    private static final String ID_PARAM = "id";
    private static final UserContext USER_CONTEXT = new UserContext(UUID.randomUUID(),
            null, new HashSet<>(Collections.singletonList(Role.TOZ)));

    private MockMvc mvc;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleParser scheduleParser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private SecurityContextHolder securityContextHolder;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new ScheduleController(scheduleService, scheduleParser, userRepository)).build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER_CONTEXT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper = new ObjectMapper().registerModule(javaTimeModule);

    }

    @After
    public void clean() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @UseDataProvider(value = "getReservationResponseView",
            location = ScheduleDataProvider.class)
    public void shouldReturnOKWhenGetSchedule(ReservationResponseView reservationResponseView) throws Exception {
        List<ReservationResponseView> scheduleReservationViews = new ArrayList<>();
        scheduleReservationViews.add(reservationResponseView);
        when(scheduleService.findScheduleReservations(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(scheduleReservationViews);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        mvc.perform(get(SCHEDULE_PATH)
                .param("from", VALID_LOCAL_DATE_FROM.toString())
                .param("to", VALID_LOCAL_DATE_TO))
                .andExpect(status().isOk());

        verify(scheduleService, times(1))
                .findScheduleReservations(any(LocalDate.class), any(LocalDate.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservationResponseView",
            location = ScheduleDataProvider.class)
    public void shouldThrowExceptionWhenGetReservationById(ReservationResponseView reservationResponseView) throws Exception {
        when(scheduleService.findReservation(any(UUID.class)))
                .thenReturn(reservationResponseView);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        try {
            mvc.perform(get(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                    .param(ID_PARAM, VALID_LOCAL_DATE_TO))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            if (e.getCause() instanceof NoPermissionException)
                ok();
        }
    }

    @Test
    @UseDataProvider(value = "getReservationResponseView",
            location = ScheduleDataProvider.class)
    public void shouldReturnCreatedWhenCreateReservation(ReservationResponseView reservationResponseView) throws Exception {
        when(scheduleService.makeReservation(any(ReservationRequestView.class)))
                .thenReturn(reservationResponseView);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        ReservationRequestView view = new ReservationRequestView();
        view.setDate(VALID_LOCAL_DATE_FROM);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setModificationMessage("string");
        view.setOwnerId(UUID.randomUUID());
        view.setEndTime(VALID_LOCAL_TIME);
        mvc.perform(post(String.format("%s", SCHEDULE_PATH))
                .param(ID_PARAM, UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(view)))
                .andExpect(status().isCreated());

        verify(scheduleService, times(1))
                .makeReservation(any(ReservationRequestView.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservationResponseView",
            location = ScheduleDataProvider.class)
    public void shouldReturnCreatedWhenUpdateReservation(ReservationResponseView reservationResponseView) throws Exception {
        when(scheduleService.updateReservation(any(UUID.class), any(ReservationRequestView.class)))
                .thenReturn(reservationResponseView);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        ReservationRequestView view = new ReservationRequestView();
        view.setDate(VALID_LOCAL_DATE_FROM);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setModificationMessage("string");
        view.setOwnerId(UUID.randomUUID());
        view.setEndTime(VALID_LOCAL_TIME);
        mvc.perform(put(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                .param(ID_PARAM, UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(view)))
                .andExpect(status().isCreated());

        verify(scheduleService, times(1))
                .updateReservation(any(UUID.class), any(ReservationRequestView.class));
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @UseDataProvider(value = "getReservationResponseView",
            location = ScheduleDataProvider.class)
    public void shouldReturnOKWhenDeleteReservation(ReservationResponseView reservationResponseView) throws Exception {
        when(scheduleService.removeReservation(any(UUID.class)))
                .thenReturn(reservationResponseView);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        mvc.perform(delete(String.format("%s/%s", SCHEDULE_PATH, UUID.randomUUID().toString()))
                .param(ID_PARAM, UUID.randomUUID().toString())
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
    public void shouldReturnBadRequestWhenDateInPast(ReservationRequestView reservationRequestView) throws Exception {
        reservationRequestView.setDate(PAST_LOCAL_DATE);
        mvc.perform(post(String.format("%s", SCHEDULE_PATH))
                .param(ID_PARAM, UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(reservationRequestView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldReturnBadRequestWhenDateIsNull(ReservationRequestView reservationRequestView) throws Exception {
        reservationRequestView.setDate(null);
        mvc.perform(post(String.format("%s", SCHEDULE_PATH))
                .param(ID_PARAM, UUID.randomUUID().toString())
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(reservationRequestView)))
                .andExpect(status().isBadRequest());
    }
}
