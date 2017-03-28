package com.intive.patronage.toz.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.intive.patronage.toz.schedule.DateUtil.convertToDate;

@Service
class ReservationService {

    //TODO: move this to configuration
    private ZoneOffset timeZoneOffset = ZoneOffset.UTC;

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Autowired
    ReservationService(ReservationRepository reservationRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    public List<Reservation> findScheduleReservations(LocalDate from, LocalDate to) {
        Date dateFrom = convertToDate(
                from,
                from.atStartOfDay().toLocalTime());
        Date dateTo = convertToDate(
                to.plusDays(1),
                to.atStartOfDay().toLocalTime());
        return reservationRepository
                .findByStartDateBetween(dateFrom, dateTo);

    }

    public Reservation makeReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
