package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
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

    private final String RESERVATION = "Reservation";
    private final String USER = "User";
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private ScheduleParser scheduleParser;
    @Value("${timezoneOffset}")
    private String zoneOffset = "Z";

    @Autowired
    ScheduleService(ReservationRepository reservationRepository,
                    ScheduleParser scheduleParser,
                    UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.scheduleParser = scheduleParser;
        this.userRepository = userRepository;
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
        ifReservationDoesntExistThrowException(id);
        return reservationRepository.findOne(id);
    }

    Reservation makeReservation(Reservation reservation) {
        ifUserDoesntExistThrowException(reservation.getOwnerUuid());
        ifUserDoesntExistThrowException(reservation.getModificationAuthorUuid());
        ifReservationExistsThrowException(reservation.getStartDate());
        scheduleParser.validateHours(reservation.getStartDate(), reservation.getEndDate());
        return reservationRepository.save(reservation);
    }

    Reservation updateReservation(UUID id, Reservation newReservation) {
        ifUserDoesntExistThrowException(newReservation.getOwnerUuid());
        ifUserDoesntExistThrowException(newReservation.getModificationAuthorUuid());
        ifReservationDoesntExistThrowException(id);
        scheduleParser.validateHours(newReservation.getStartDate(), newReservation.getEndDate());
        Reservation reservation = reservationRepository.findOne(id);
        reservation.setStartDate(newReservation.getStartDate());
        reservation.setEndDate(newReservation.getEndDate());
        reservation.setOwnerUuid(newReservation.getOwnerUuid());
        reservation.setModificationAuthorUuid(newReservation.getModificationAuthorUuid());
        reservation.setModificationMessage(newReservation.getModificationMessage());
        return reservationRepository.save(reservation);
    }

    Reservation removeReservation(UUID id) {
        ifReservationDoesntExistThrowException(id);
        Reservation deletedReservation = reservationRepository.findOne(id);
        reservationRepository.delete(id);
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

    private void ifEntityDoesntExistThrowException(UUID id, IdentifiableRepository repo, String entityName){
        if (!repo.exists(id)){
            throw new NotFoundException(entityName);
        }
    }

    private void ifUserDoesntExistThrowException(UUID userId) {
        if (!userRepository.exists(userId)) {
            throw new NotFoundException(USER);
        }
    }

    private void ifReservationDoesntExistThrowException(UUID userId) {
        if (!reservationRepository.exists(userId)) {
            throw new NotFoundException(RESERVATION);
        }
    }
}
