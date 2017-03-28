package com.intive.patronage.toz.schedule.service;

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

import static com.intive.patronage.toz.schedule.DateUtil.convertToDate;

@Service
@PropertySource("classpath:application.properties")
public class ReservationService {

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

    public Reservation makeReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
