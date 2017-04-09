package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.intive.patronage.toz.users.UserRepository;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.intive.patronage.toz.schedule.ScheduleDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class ScheduleServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScheduleParser scheduleParser;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleService = new ScheduleService(reservationRepository, scheduleParser, userRepository);
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
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationList(Reservation reservation) {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository
                .findByStartDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(reservations);
        List<Reservation> foundReservations =
                scheduleService.findScheduleReservations(LOCAL_DATE_FROM, LOCAL_DATE_TO);
        assertThat(foundReservations).isNotEmpty();
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationWhenFindById(Reservation reservation) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(reservation);
        Reservation foundReservation =
                scheduleService.findReservation(UUID.randomUUID());
        assertThat(foundReservation).isEqualToComparingFieldByField(reservation);
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldUpdateReservation(Reservation reservation) {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(reservation);
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        Reservation updatedReservation =
                scheduleService.updateReservation(UUID.randomUUID(), reservation);
        assertThat(updatedReservation).isEqualToComparingFieldByField(reservation);
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldCreateReservation(Reservation reservation) {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        Reservation createdReservation =
                scheduleService.makeReservation(reservation);
        assertThat(createdReservation).isEqualToComparingFieldByField(reservation);
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservationRepository, times(1)).findByStartDate(any(Date.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldDeleteReservation(Reservation reservation) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(reservation);
        doNothing().when(reservationRepository).delete(any(UUID.class));
        Reservation deletedReservation = scheduleService.removeReservation(UUID.randomUUID());
        assertThat(deletedReservation).isEqualToComparingFieldByField(reservation);
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
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

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenUserOwnerNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), new Reservation());
    }
}
