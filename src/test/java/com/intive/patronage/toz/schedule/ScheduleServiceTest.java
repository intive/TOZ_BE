package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.repository.ReservationRepository;
import com.intive.patronage.toz.schedule.service.ScheduleService;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class ScheduleServiceTest {

    private static final LocalDate LOCAL_DATE_FROM = LocalDate.parse("2017-03-01");
    private static final LocalDate LOCAL_DATE_TO = LocalDate.parse("2018-03-01");

    private static final Date START_DATE = Date.from(LocalDate.parse("2017-03-01").atStartOfDay().toInstant(ZoneOffset.UTC));
    private static final Date END_DATE = Date.from(LocalDate.parse("2017-03-02").atStartOfDay().toInstant(ZoneOffset.UTC));
    private static final UUID OWNER_UUID = UUID.randomUUID();
    private static final UUID MODIFICATION_AUTHOR_UUID = UUID.randomUUID();
    private static final String MODIFICATION_MESSAGE = "string";

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ScheduleParser scheduleParser;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleService = new ScheduleService(reservationRepository, scheduleParser);
    }

    @DataProvider
    public static Object[] getReservationObject() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(START_DATE);
        reservation.setEndDate(END_DATE);
        reservation.setModificationMessage(MODIFICATION_MESSAGE);
        reservation.setModificationAuthorUuid(MODIFICATION_AUTHOR_UUID);
        reservation.setOwnerUuid(OWNER_UUID);
        return new Reservation[]{reservation};
    }

    @Test
    public void shouldReturnEmptyListWhenNoReservationsInDb() {
        when(reservationRepository
                .findByStartDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        List<Reservation> reservations =
                scheduleService.findScheduleReservations(LOCAL_DATE_FROM, LOCAL_DATE_TO);
        assertThat(reservations).isEmpty();
    }

    @Test
    @UseDataProvider("getReservationObject")
    public void shouldReturnReservationWhenFindById(Reservation reservation) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(reservation);
        Reservation foundReservation = scheduleService.findReservation(UUID.randomUUID());

        assertThat(foundReservation).isNotNull();
        assertThat(foundReservation.getStartDate()).isEqualTo(START_DATE);
        assertThat(foundReservation.getEndDate()).isEqualTo(END_DATE);
        assertThat(foundReservation.getModificationAuthorUuid()).isEqualTo(MODIFICATION_AUTHOR_UUID);
        assertThat(foundReservation.getModificationMessage()).isEqualTo(MODIFICATION_MESSAGE);
        assertThat(foundReservation.getOwnerUuid()).isEqualTo(OWNER_UUID);

        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    @UseDataProvider("getReservationObject")
    public void shouldUpdateReservation(Reservation reservation) {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(reservation);
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        Reservation updatedReservation =
                scheduleService.updateReservation(UUID.randomUUID(), reservation);

        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getStartDate()).isEqualTo(START_DATE);
        assertThat(updatedReservation.getEndDate()).isEqualTo(END_DATE);
        assertThat(updatedReservation.getModificationAuthorUuid()).isEqualTo(MODIFICATION_AUTHOR_UUID);
        assertThat(updatedReservation.getModificationMessage()).isEqualTo(MODIFICATION_MESSAGE);
        assertThat(updatedReservation.getOwnerUuid()).isEqualTo(OWNER_UUID);

        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    public void shouldDeleteReservation() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        scheduleService.removeReservation(UUID.randomUUID());

        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).delete(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenUpdateReservationNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), new Reservation());
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenRemoveReservationNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.removeReservation(UUID.randomUUID());
    }
}
