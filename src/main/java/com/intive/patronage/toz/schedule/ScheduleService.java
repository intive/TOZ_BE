package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.constant.OperationType;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservationChangelog;
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

    List<ScheduleReservation> findScheduleReservations(LocalDate from, LocalDate to) {
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

    ScheduleReservation findReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        return reservationRepository.findOne(id);
    }

    ScheduleReservation makeReservation(ScheduleReservation scheduleReservation, String modificationMessage) {
        ifEntityDoesntExistInRepoThrowException(scheduleReservation.getOwnerUuid(), userRepository, USER);
        ifReservationExistsThrowException(scheduleReservation.getStartDate());
        scheduleParser.validateHours(scheduleReservation.getStartDate(), scheduleReservation.getEndDate());
        ScheduleReservation newScheduleReservation = reservationRepository.save(scheduleReservation);
        saveReservationChangelog(newScheduleReservation, modificationMessage, OperationType.CREATE);
        return newScheduleReservation;
    }

    ScheduleReservation updateReservation(UUID id, ScheduleReservation newScheduleReservation, String modificationMessage) {
        ifEntityDoesntExistInRepoThrowException(newScheduleReservation.getOwnerUuid(), userRepository, USER);
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        scheduleParser.validateHours(newScheduleReservation.getStartDate(), newScheduleReservation.getEndDate());
        ScheduleReservation scheduleReservation = reservationRepository.findOne(id);
        scheduleReservation.setStartDate(newScheduleReservation.getStartDate());
        scheduleReservation.setEndDate(newScheduleReservation.getEndDate());
        scheduleReservation.setOwnerUuid(newScheduleReservation.getOwnerUuid());
        ScheduleReservation updatedScheduleReservation = reservationRepository.save(scheduleReservation);
        saveReservationChangelog(updatedScheduleReservation, modificationMessage, OperationType.UPDATE);
        return updatedScheduleReservation;
    }

    ScheduleReservation removeReservation(UUID id) {
        ifEntityDoesntExistInRepoThrowException(id, reservationRepository, RESERVATION);
        ScheduleReservation deletedScheduleReservation = reservationRepository.findOne(id);
        reservationRepository.delete(id);
        saveReservationChangelog(deletedScheduleReservation, null, OperationType.DELETE);
        return deletedScheduleReservation;
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
}
