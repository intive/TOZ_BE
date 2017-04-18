package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.constant.OperationType;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservationChangelog;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.intive.patronage.toz.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.util.DateUtil.convertToDate;

@Service
@PropertySource("classpath:application.properties")
class ScheduleService {

    private static final String RESERVATION = "ScheduleReservation";
    private final String USER = "User";
    private final ReservationRepository reservationRepository;
    private final ReservationChangelogRepository reservationChangelogRepository;
    private final UserRepository userRepository;
    private ScheduleParser scheduleParser;
    @Value("${timezoneOffset}")
    private String zoneOffset = "Z";

    @Autowired
    ScheduleService(ReservationRepository reservationRepository,
                    ScheduleParser scheduleParser,
                    UserRepository userRepository,
                    ReservationChangelogRepository reservationChangelogRepository) {
        this.reservationRepository = reservationRepository;
        this.scheduleParser = scheduleParser;
        this.userRepository = userRepository;
        this.reservationChangelogRepository = reservationChangelogRepository;
    }

    List<ReservationResponseView> findScheduleReservations(LocalDate from, LocalDate to) {
        Date dateFrom = convertToDate(
                from,
                from.atStartOfDay().toLocalTime(),
                ZoneOffset.of(zoneOffset));
        Date dateTo = convertToDate(
                to,
                LocalDate.now().atTime(LocalTime.MAX).toLocalTime(),
                ZoneOffset.of(zoneOffset));
        List<ReservationResponseView> reservationViews = new ArrayList<>();
        for (ScheduleReservation reservation : reservationRepository.findByStartDateBetween(dateFrom, dateTo)) {
            reservationViews.add(convertToReservationResponseView(reservation));
        }
        return reservationViews;
    }

    ReservationResponseView findReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        return convertToReservationResponseView(reservationRepository.findOne(id));
    }

    ReservationResponseView makeReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation scheduleReservation = convertToReservation(reservationRequestView);
        ifEntityDoesntExistInRepoThrowException(scheduleReservation.getOwnerUuid(), userRepository, USER);
        ifReservationExistsThrowException(scheduleReservation.getStartDate());
        scheduleParser.validateHours(scheduleReservation.getStartDate(), scheduleReservation.getEndDate());
        ScheduleReservation newScheduleReservation = reservationRepository.save(scheduleReservation);
        saveReservationChangelog(newScheduleReservation, reservationRequestView.getModificationMessage(), OperationType.CREATE);
        return convertToReservationResponseView(newScheduleReservation);
    }

    ReservationResponseView updateReservation(UUID id, ReservationRequestView reservationRequestView) {
        ScheduleReservation newScheduleReservation = convertToReservation(reservationRequestView);
        ifEntityDoesntExistInRepoThrowException(newScheduleReservation.getOwnerUuid(), userRepository, USER);
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        scheduleParser.validateHours(newScheduleReservation.getStartDate(), newScheduleReservation.getEndDate());
        ScheduleReservation scheduleReservation = reservationRepository.findOne(id);
        scheduleReservation.setStartDate(newScheduleReservation.getStartDate());
        scheduleReservation.setEndDate(newScheduleReservation.getEndDate());
        scheduleReservation.setOwnerUuid(newScheduleReservation.getOwnerUuid());
        ScheduleReservation updatedScheduleReservation = reservationRepository.save(scheduleReservation);
        saveReservationChangelog(updatedScheduleReservation, reservationRequestView.getModificationMessage(), OperationType.UPDATE);
        return convertToReservationResponseView(updatedScheduleReservation);
    }

    ReservationResponseView removeReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        ScheduleReservation deletedScheduleReservation = reservationRepository.findOne(id);
        reservationRepository.delete(id);
        saveReservationChangelog(deletedScheduleReservation, null, OperationType.DELETE);
        return convertToReservationResponseView(deletedScheduleReservation);
    }

    private void ifReservationExistsThrowException(Date reservationDate) {
        if (reservationRepository.findByStartDate(reservationDate).size() > 0) {
            LocalTime time = reservationDate
                    .toInstant()
                    .atOffset(ZoneOffset.of(zoneOffset))
                    .toLocalTime();
            LocalDate day = reservationDate
                    .toInstant()
                    .atOffset(ZoneOffset.of(zoneOffset))
                    .toLocalDate();
            throw new ReservationAlreadyExistsException(time, day);
        }
    }

    private void ifEntityDoesntExistInRepoThrowException(UUID id, IdentifiableRepository repo, String entityName) {
        if (!repo.exists(id)) {
            throw new NotFoundException(entityName);
        }
    }

    private void saveReservationChangelog(ScheduleReservation scheduleReservation, String modificationMessage, OperationType operationType) {
        ScheduleReservationChangelog newLog = new ScheduleReservationChangelog();
//        TODO: waits for security users implementation, to get info from Principal
//        newLog.setModificationAuthorUuid(...);
//        ifEntityDoesntExistInRepoThrowException(newReservation.getModificationAuthorUuid(), userRepository, USER);
        newLog.setModificationMessage(modificationMessage);
        newLog.setOperationType(operationType);
        newLog.setReservationId(scheduleReservation.getId());
        newLog.setOwnerUuid(scheduleReservation.getOwnerUuid());
        newLog.setStartDate(scheduleReservation.getStartDate());
        newLog.setEndDate(scheduleReservation.getEndDate());
        reservationChangelogRepository.save(newLog);
    }

    public ScheduleReservation convertToReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation scheduleReservation = new ScheduleReservation();
        scheduleReservation.setStartDate(
                convertToDate(
                        reservationRequestView.getDate(),
                        reservationRequestView.getStartTime(),
                        ZoneOffset.of(zoneOffset)));
        scheduleReservation.setEndDate(
                convertToDate(
                        reservationRequestView.getDate(),
                        reservationRequestView.getEndTime(),
                        ZoneOffset.of(zoneOffset)));
        scheduleReservation.setOwnerUuid(reservationRequestView.getOwnerId());
        return scheduleReservation;
    }

    public ReservationResponseView convertToReservationResponseView(ScheduleReservation scheduleReservation) {
        ReservationResponseView reservationResponseView = new ReservationResponseView();
        reservationResponseView.setDate(scheduleReservation.getStartDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalDate());
        reservationResponseView.setStartTime(scheduleReservation.getStartDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalTime());
        reservationResponseView.setEndTime(scheduleReservation.getEndDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalTime());
        reservationResponseView.setOwnerId(scheduleReservation
                .getOwnerUuid());
        reservationResponseView.setCreated(scheduleReservation
                .getCreationDate()
                .getTime());
        reservationResponseView.setLastModified(scheduleReservation
                .getModificationDate()
                .getTime());
        reservationResponseView.setId(scheduleReservation.getId());
        reservationResponseView.setOwnerName(
                userRepository.findOne(scheduleReservation.getOwnerUuid()).getName());
        reservationResponseView.setOwnerSurname(
                userRepository.findOne(scheduleReservation.getOwnerUuid()).getSurname());
        return reservationResponseView;
    }

}
