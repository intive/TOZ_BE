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
import java.util.UUID;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {

    private final ReservationService reservationService;

    @Autowired
    public ScheduleController(ReservationService reservationService) {
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

//        return reservationService.findScheduleReservations(from, to);
        return new ScheduleView(null, null);
    }

    @ApiOperation("Get single reservation by id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ReservationView getReservation(@PathVariable UUID id) {
        return new ReservationView();
    }

    @ApiOperation("Make reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "Already exists", response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationView> makeReservation(@Valid @RequestBody ReservationView reservation) {
        // TODO remove once service is done
        ReservationView createdReservation = reservationService.makeReservation(reservation);
//        createdReservation.setId(UUID.randomUUID());
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
        return new ReservationView();
    }

    @ApiOperation("Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationView removeReservation(@PathVariable UUID id) {
        return new ReservationView();
    }
}
