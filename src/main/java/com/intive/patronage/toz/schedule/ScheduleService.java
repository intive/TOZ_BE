package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.constant.OperationType;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.model.db.ReservationChangelog;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.intive.patronage.toz.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.util.DateUtil.convertToDate;

@Service
@PropertySource("classpath:application.properties")
class ScheduleService {

    private static final String RESERVATION = "Reservation";
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

    List<Reservation> findScheduleReservations(LocalDate from, LocalDate to) {
        Date dateFrom = convertToDate(
                from,
                from.atStartOfDay().toLocalTime(),
                ZoneOffset.of(zoneOffset));
        Date dateTo = convertToDate(
                to,
                LocalDate.now().atTime(LocalTime.MAX).toLocalTime(),
                ZoneOffset.of(zoneOffset));

        return reservationRepository.findByStartDateBetween(dateFrom, dateTo);
    }

    Reservation findReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        return reservationRepository.findOne(id);
    }

    Reservation makeReservation(Reservation reservation, String modificationMessage) {
        ifEntityDoesntExistInRepoThrowException(reservation.getOwnerUuid(), userRepository, USER);
        ifReservationExistsThrowException(reservation.getStartDate());
        scheduleParser.validateHours(reservation.getStartDate(), reservation.getEndDate());
        Reservation newReservation = reservationRepository.save(reservation);
        saveReservationChangelog(newReservation, modificationMessage, OperationType.CREATE);
        return newReservation;
    }

    Reservation updateReservation(UUID id, Reservation newReservation, String modificationMessage) {
        ifEntityDoesntExistInRepoThrowException(newReservation.getOwnerUuid(), userRepository, USER);
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        scheduleParser.validateHours(newReservation.getStartDate(), newReservation.getEndDate());
        Reservation reservation = reservationRepository.findOne(id);
        reservation.setStartDate(newReservation.getStartDate());
        reservation.setEndDate(newReservation.getEndDate());
        reservation.setOwnerUuid(newReservation.getOwnerUuid());
        Reservation updatedReservation = reservationRepository.save(reservation);
        saveReservationChangelog(updatedReservation, modificationMessage, OperationType.UPDATE);
        return updatedReservation;
    }

    Reservation removeReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        Reservation deletedReservation = reservationRepository.findOne(id);
        reservationRepository.delete(id);
        saveReservationChangelog(deletedReservation, null, OperationType.DELETE);
        return deletedReservation;
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

    private void saveReservationChangelog(Reservation reservation, String modificationMessage, OperationType operationType) {
        ReservationChangelog newLog = new ReservationChangelog();
//        TODO: waits for security users implementation, to get info from Principal
//        newLog.setModificationAuthorUuid(...);
//        ifEntityDoesntExistInRepoThrowException(newReservation.getModificationAuthorUuid(), userRepository, USER);
        newLog.setModificationMessage(modificationMessage);
        newLog.setOperationType(operationType);
        newLog.setReservationId(reservation.getId());
        newLog.setOwnerUuid(reservation.getOwnerUuid());
        newLog.setStartDate(reservation.getStartDate());
        newLog.setEndDate(reservation.getEndDate());
        reservationChangelogRepository.save(newLog);
    }
}
