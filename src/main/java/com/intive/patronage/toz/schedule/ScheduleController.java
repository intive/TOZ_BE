package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.ArgumentErrorResponse;
import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.DateUtil.convertToDate;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {


    //TODO: move this to configuration
    private static ZoneOffset timeZoneOffset = ZoneOffset.UTC;

    private final ReservationService reservationService;

    @Autowired
    ScheduleController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ApiOperation("Get schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ScheduleView getSchedule(@ApiParam(value = "Date in UTC, format: yyyy-MM-dd", required = true)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                    @RequestParam("from") LocalDate from,
                                    @ApiParam(value = "Date in UTC, format: yyyy-MM-dd", required = true)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                    @RequestParam("to") LocalDate to) {

        List<Reservation> reservations = reservationService.findScheduleReservations(from, to);
        List<ReservationView> reservationViews = new ArrayList<ReservationView>();
        for (Reservation reservation : reservations) {
            reservationViews.add(convertToReservationView(reservation));
        }
        List<DayConfigView> dayConfigs = new ArrayList<>();
        return new ScheduleView(reservationViews, dayConfigs);
    }

    @ApiOperation("Get single reservation by id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ReservationView getReservation(@PathVariable UUID id) {
        return null;
    }

    @ApiOperation("Make reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "Already exists", response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationView> makeReservation(@Valid @RequestBody ReservationView reservationView) {
        ReservationView createdReservation = convertToReservationView(
                reservationService.makeReservation(convertToReservation(reservationView)));
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        final String reservationLocation = String.format("%s/%s", baseLocation, createdReservation.getId());
        final URI location = UriComponentsBuilder.fromUriString(reservationLocation)
                .build()
                .toUri();
        return ResponseEntity.created(location)
                .body(createdReservation);
    }

    @ApiOperation("Update reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationView updateReservation(@PathVariable UUID id,
                                             @Valid @RequestBody ReservationView reservation) {
        return null;
    }

    @ApiOperation("Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationView removeReservation(@PathVariable UUID id) {
        return null;
    }

    private static Reservation convertToReservation(ReservationView reservationView) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(
                convertToDate(
                        reservationView.getDate(),
                        reservationView.getStartTime()));
        reservation.setEndDate(
                convertToDate(
                        reservationView.getDate(),
                        reservationView.getEndTime()));
        reservation.setOwnerUuid(reservationView.getOwnerId());
        reservation.setModificationMessage(reservationView.getModificationMessage());
        reservation.setModificationAuthorUuid(reservationView.getModificationAuthorId());
        return reservation;
    }

    private static ReservationView convertToReservationView(Reservation reservation) {
        ReservationView reservationView = new ReservationView();
        reservationView.setDate(reservation
                .getStartDate()
                .toInstant()
                .atOffset(timeZoneOffset)
                .toLocalDate());
        reservationView.setStartTime(reservation
                .getStartDate()
                .toInstant()
                .atOffset(timeZoneOffset)
                .toLocalTime());
        reservationView.setEndTime(reservation
                .getEndDate()
                .toInstant()
                .atOffset(timeZoneOffset)
                .toLocalTime());
        reservationView.setOwnerId(reservation
                .getOwnerUuid());
        reservationView.setCreated(reservation
                .getCreationDate()
                .getTime());
        reservationView.setLastModified(reservation
                .getModificationDate()
                .getTime());
        reservationView.setModificationMessage(reservation
                .getModificationMessage());
        reservationView.setModificationAuthorId(reservation
                .getModificationAuthorUuid());
        reservationView.setId(reservation.getId());
        return reservationView;
    }
}
