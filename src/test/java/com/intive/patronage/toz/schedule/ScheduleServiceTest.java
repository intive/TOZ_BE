package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservationChangelog;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
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
        List<ReservationResponseView> scheduleReservations =
                scheduleService.findScheduleReservations(VALID_LOCAL_DATE_FROM, VALID_LOCAL_DATE_TO);
        assertThat(scheduleReservations).isEmpty();
    }

    @Test
    @UseDataProvider(value = "getReservation",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationList(ScheduleReservation scheduleReservation) {
        List<ScheduleReservation> scheduleReservations = new ArrayList<>();
        scheduleReservations.add(scheduleReservation);
        when(reservationRepository.findByStartDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(scheduleReservations);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        List<ReservationResponseView> foundScheduleReservations =
                scheduleService.findScheduleReservations(VALID_LOCAL_DATE_FROM, VALID_LOCAL_DATE_TO);
        assertThat(foundScheduleReservations).isNotEmpty();
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldReturnReservationWhenFindById(ReservationRequestView reservationRequestView) {
        ScheduleReservation returnReservation = scheduleService.convertToReservation(reservationRequestView);
        returnReservation.setCreationDate(VALID_DATE_FROM);
        returnReservation.setModificationDate(VALID_DATE_FROM);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        when(reservationRepository.exists(any(UUID.class)))
                .thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class)))
                .thenReturn(returnReservation);
        ReservationResponseView foundScheduleReservationResponseView =
                scheduleService.findReservation(UUID.randomUUID());
        assertThat(foundScheduleReservationResponseView.getOwnerName()).isEqualTo(EXAMPLE_USER.getName());
        assertThat(foundScheduleReservationResponseView.getOwnerSurname()).isEqualTo(EXAMPLE_USER.getSurname());
        assertThat(foundScheduleReservationResponseView.getDate()).isEqualTo(reservationRequestView.getDate());
        assertThat(foundScheduleReservationResponseView.getStartTime()).isEqualTo(reservationRequestView.getStartTime());
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldUpdateReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation returnReservation = scheduleService.convertToReservation(reservationRequestView);
        returnReservation.setCreationDate(VALID_DATE_FROM);
        returnReservation.setModificationDate(VALID_DATE_FROM);
        when(reservationRepository.save(any(ScheduleReservation.class)))
                .thenReturn(returnReservation);
        when(reservationRepository.findOne(any(UUID.class)))
                .thenReturn(returnReservation);
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        ReservationResponseView updatedReservationResponseView =
                scheduleService.updateReservation(UUID.randomUUID(), reservationRequestView);
        assertThat(updatedReservationResponseView.getOwnerName()).isEqualTo(EXAMPLE_USER.getName());
        assertThat(updatedReservationResponseView.getOwnerSurname()).isEqualTo(EXAMPLE_USER.getSurname());
        assertThat(updatedReservationResponseView.getDate()).isEqualTo(reservationRequestView.getDate());
        assertThat(updatedReservationResponseView.getStartTime()).isEqualTo(reservationRequestView.getStartTime());
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(userRepository, times(2)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).findByStartDate(any(Date.class));
        verify(reservationRepository, times(1)).save(any(ScheduleReservation.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldCreateReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation returnReservation = scheduleService.convertToReservation(reservationRequestView);
        returnReservation.setCreationDate(VALID_DATE_FROM);
        returnReservation.setModificationDate(VALID_DATE_FROM);
        when(reservationRepository.save(any(ScheduleReservation.class)))
                .thenReturn(returnReservation);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);
        ReservationResponseView createdScheduleReservation =
                scheduleService.makeReservation(reservationRequestView);
        assertThat(createdScheduleReservation.getOwnerName()).isEqualTo(EXAMPLE_USER.getName());
        assertThat(createdScheduleReservation.getOwnerSurname()).isEqualTo(EXAMPLE_USER.getSurname());
        assertThat(createdScheduleReservation.getDate()).isEqualTo(reservationRequestView.getDate());
        assertThat(createdScheduleReservation.getStartTime()).isEqualTo(reservationRequestView.getStartTime());
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(userRepository, times(1)).exists(any(UUID.class));
        verify(userRepository, times(2)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).save(any(ScheduleReservation.class));
        verify(reservationRepository, times(1)).findByStartDate(any(Date.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldDeleteReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation returnReservation = scheduleService.convertToReservation(reservationRequestView);
        returnReservation.setCreationDate(VALID_DATE_FROM);
        returnReservation.setModificationDate(VALID_DATE_FROM);
        when(userRepository.findOne(any(UUID.class)))
                .thenReturn(EXAMPLE_USER);
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(reservationRepository.findOne(any(UUID.class))).thenReturn(returnReservation);
        doNothing().when(reservationRepository).delete(any(UUID.class));
        ReservationResponseView deletedReservationResponseView = scheduleService.removeReservation(UUID.randomUUID());
        assertThat(deletedReservationResponseView.getOwnerName()).isEqualTo(EXAMPLE_USER.getName());
        assertThat(deletedReservationResponseView.getOwnerSurname()).isEqualTo(EXAMPLE_USER.getSurname());
        assertThat(deletedReservationResponseView.getDate()).isEqualTo(reservationRequestView.getDate());
        assertThat(deletedReservationResponseView.getStartTime()).isEqualTo(reservationRequestView.getStartTime());
        verify(reservationChangelogRepository, times(1)).save(any(ScheduleReservationChangelog.class));
        verify(userRepository, times(2)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).exists(any(UUID.class));
        verify(reservationRepository, times(1)).findOne(any(UUID.class));
        verify(reservationRepository, times(1)).delete(any(UUID.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider(value = "getReservationRequestView",
                    location = ScheduleDataProvider.class)
    public void shouldReturnErrorWhenUpdateReservationNotFound(ReservationRequestView reservationRequestView) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), reservationRequestView);
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnErrorWhenRemoveReservationNotFound() {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.removeReservation(UUID.randomUUID());
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider(value = "getReservationRequestView",
            location = ScheduleDataProvider.class)
    public void shouldReturnErrorWhenUserOwnerNotFound(ReservationRequestView reservationRequestView) {
        when(reservationRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(false);
        scheduleService.updateReservation(UUID.randomUUID(), reservationRequestView);
        verify(reservationChangelogRepository, times(0)).save(any(ScheduleReservationChangelog.class));
    }
}
