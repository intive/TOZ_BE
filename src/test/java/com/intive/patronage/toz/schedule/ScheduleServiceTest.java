package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservationChangelog;
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
    private ReservationChangelogRepository reservationChangelogRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScheduleParser scheduleParser;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleService = new ScheduleService(reservationRepository, scheduleParser, userRepository, reservationChangelogRepository);
    }


    @Test
    public void shouldReturnEmptyListWhenNoReservationsInDb() {
        when(reservationRepository
                .findByStartDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        List<ScheduleReservation> scheduleReservations =
                scheduleService.findScheduleReservations(LOCAL_DATE_FROM, LOCAL_DATE_TO);
        assertThat(scheduleReservations).isEmpty();
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationList(ScheduleReservation scheduleReservation) {
        List<ScheduleReservation> scheduleReservations = new ArrayList<>();
        scheduleReservations.add(scheduleReservation);
        when(reservationRepository
                .findByStartDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(scheduleReservations);
        List<ScheduleReservation> foundScheduleReservations =
                scheduleService.findScheduleReservations(LOCAL_DATE_FROM, LOCAL_DATE_TO);
        assertThat(foundScheduleReservations).isNotEmpty();
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationWhenFindById(ScheduleReservation scheduleReservation) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(scheduleReservation);
        ScheduleReservation foundScheduleReservation =
                scheduleService.findReservation(UUID.randomUUID());
        assertThat(foundScheduleReservation).isEqualToComparingFieldByField(scheduleReservation);
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldUpdateReservation(ScheduleReservation scheduleReservation) {
        when(reservationRepository.save(any(ScheduleReservation.class))).thenReturn(scheduleReservation);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(scheduleReservation);
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        ScheduleReservation updatedScheduleReservation =
                scheduleService.updateReservation(UUID.randomUUID(), scheduleReservation, MODIFICATION_MESSAGE);
        assertThat(updatedScheduleReservation).isEqualToComparingFieldByField(scheduleReservation);
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(ScheduleReservation.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldCreateReservation(ScheduleReservation scheduleReservation) {
        when(reservationRepository.save(any(ScheduleReservation.class))).thenReturn(scheduleReservation);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        ScheduleReservation createdScheduleReservation =
                scheduleService.makeReservation(scheduleReservation, MODIFICATION_MESSAGE);
        assertThat(createdScheduleReservation).isEqualToComparingFieldByField(scheduleReservation);
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(ScheduleReservation.class));
        verify(reservationRepository, times(1)).findByStartDate(any(Date.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldDeleteReservation(ScheduleReservation scheduleReservation) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(scheduleReservation);
        doNothing().when(reservationRepository).delete(any(UUID.class));
        ScheduleReservation deletedScheduleReservation = scheduleService.removeReservation(UUID.randomUUID());
        assertThat(deletedScheduleReservation).isEqualToComparingFieldByField(scheduleReservation);
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).delete(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenUpdateReservationNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), new ScheduleReservation(), anyString());
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenRemoveReservationNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.removeReservation(UUID.randomUUID());
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenUserOwnerNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), new ScheduleReservation(), anyString());
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }
}
