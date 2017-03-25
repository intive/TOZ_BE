package com.intive.patronage.toz.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class ReservationService {

    //TODO: move this to configuration
    private ZoneOffset timeZoneOffset = ZoneOffset.UTC;

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Autowired
    ReservationService(ReservationRepository reservationRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

//    public ScheduleView findScheduleReservations(LocalDate from, LocalDate to){
//        return List<Reservation> reservations;
//    }

    public ReservationView makeReservation(ReservationView reservationView) {
        Reservation reservation = convertToReservation(reservationView);
        reservationRepository.save(reservation);
    }

    private Reservation convertToReservation(ReservationView reservationView) {
        return Reservation.builder()
                .startDate(convertToDate(
                        reservationView.getDate(),
                        reservationView.getStartTime()))
                .endDate(convertToDate(
                        reservationView.getDate(),
                        reservationView.getEndTime()))
                .ownerUuid(reservationView
                        .getOwner()
                        .getId())
                .creationDate(Date.from(reservationView
                        .getCreated()
                        .toInstant(timeZoneOffset)))
                .modificationDate(Date.from(reservationView
                        .getLastModified()
                        .toInstant(timeZoneOffset)))
                .modificationMessage(reservationView
                        .getModificationMessage())
                .modificationAuthorUuid(reservationView
                        .getModificationAuthor()
                        .getId())
                .build();
    }

    private ReservationView convertToReservationView(Reservation reservation) {
        return ReservationView.builder()
                .date(reservation
                        .getStartDate()
                        .toInstant()
                        .atOffset(timeZoneOffset)
                        .toLocalDate())
                .startTime(reservation
                        .getStartDate()
                        .toInstant()
                        .atOffset(timeZoneOffset)
                        .toLocalTime())
                .endTime(reservation
                        .getEndDate()
                        .toInstant()
                        .atOffset(timeZoneOffset)
                        .toLocalTime())
                .owner(userService
                        .findUser(reservation.getOwnerUuid()))
                .created(reservation
                        .getCreationDate()
                        .toInstant()
                        .atOffset(timeZoneOffset)
                        .toLocalDateTime())
                .lastModified(reservation
                        .getModificationDate()
                        .toInstant()
                        .atOffset(timeZoneOffset)
                        .toLocalDateTime())
                .modificationMessage(reservation
                        .getModificationMessage())
                .modificationAuthor(userService
                        .findUser(reservation
                                .getModificationAuthorUuid()))
                .build();
    }

    private Date convertToDate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.toInstant(timeZoneOffset));
    }
}
