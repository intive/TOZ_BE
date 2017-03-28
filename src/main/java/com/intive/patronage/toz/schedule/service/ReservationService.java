package com.intive.patronage.toz.schedule.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.DateUtil.convertToDate;

@Service
@PropertySource("classpath:application.properties")
public class ReservationService {

    private final String RESERVATION = "Reservation";
    private final ReservationRepository reservationRepository;
    private ZoneOffset zoneOffset;

    @Autowired
    ReservationService(ReservationRepository reservationRepository,
                       @Value("${timezoneOffset}") String offsetString) {
        this.reservationRepository = reservationRepository;
        this.zoneOffset = ZoneOffset.of(offsetString);
    }

    public List<Reservation> findScheduleReservations(LocalDate from, LocalDate to) {
        Date dateFrom = convertToDate(
                from,
                from.atStartOfDay().toLocalTime(),
                zoneOffset);
        Date dateTo = convertToDate(
                to.plusDays(1),
                to.atStartOfDay().toLocalTime(),
                zoneOffset);
        return reservationRepository
                .findByStartDateBetween(dateFrom, dateTo);

    }

    public Reservation findReservation(UUID id) {
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
        return reservationRepository.findOne(id);
    }

    public Reservation makeReservation(Reservation reservation) {
        //TODO: validate reservation already exists
        //TODO: validate correct start and end time
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(UUID id, Reservation newReservation) {
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
        Reservation reservation = reservationRepository.findOne(id);
        reservation.setStartDate(newReservation.getStartDate());
        reservation.setEndDate(newReservation.getEndDate());
        reservation.setOwnerUuid(newReservation.getOwnerUuid());
        reservation.setModificationAuthorUuid(newReservation.getModificationAuthorUuid());
        reservation.setModificationMessage(newReservation.getModificationMessage());
        return reservationRepository.save(reservation);
    }

    public void removeReservation(UUID id){
        if (!reservationRepository.exists(id)) {
            throw new NotFoundException(RESERVATION);
        }
        reservationRepository.delete(id);
    }
}
