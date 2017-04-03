package com.intive.patronage.toz.schedule.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.schedule.excception.ReservationAlreadyExistsException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.repository.ReservationRepository;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
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
public class ScheduleService {

    private final String RESERVATION = "Reservation";
    private final ReservationRepository reservationRepository;
    private ScheduleParser scheduleParser;
    @Value("${timezoneOffset}")
    private String zoneOffset = "Z";

    @Autowired
    public ScheduleService(ReservationRepository reservationRepository,
                    ScheduleParser scheduleParser) {
        this.reservationRepository = reservationRepository;
        this.scheduleParser = scheduleParser;
    }

    public List<Reservation> findScheduleReservations(LocalDate from, LocalDate to) {
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

    public Reservation findReservation(UUID id) {
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
        return reservationRepository.findOne(id);
    }

    public Reservation makeReservation(Reservation reservation) {
        ifReservationExistsThrowException(reservation.getStartDate());
        scheduleParser.validateHours(reservation.getStartDate(), reservation.getEndDate());
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(UUID id, Reservation newReservation) {
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
        scheduleParser.validateHours(newReservation.getStartDate(), newReservation.getEndDate());
        Reservation reservation = reservationRepository.findOne(id);
        reservation.setStartDate(newReservation.getStartDate());
        reservation.setEndDate(newReservation.getEndDate());
        reservation.setOwnerUuid(newReservation.getOwnerUuid());
        reservation.setModificationAuthorUuid(newReservation.getModificationAuthorUuid());
        reservation.setModificationMessage(newReservation.getModificationMessage());
        return reservationRepository.save(reservation);
    }

    public Reservation removeReservation(UUID id) {
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
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
}
