package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.model.ArgumentErrorResponse;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.schedule.model.view.ScheduleView;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
class ScheduleController {

    @Autowired
    public ScheduleController() {
        // TODO initialize service
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

        return new ScheduleView(Collections.emptyList(), Collections.emptyList());
    }

    @ApiOperation("Get single reservation by id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ReservationResponseView getReservation(@PathVariable UUID id) {
        return new ReservationResponseView();
    }

    @ApiOperation("Make reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "Already exists", response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationResponseView> makeReservation(
            @Valid @RequestBody ReservationRequestView reservation) {

        // TODO remove once service is done
        ReservationResponseView createdReservation = new ReservationResponseView();
        createdReservation.setId(UUID.randomUUID());
        createdReservation.setCreated(LocalDateTime.now());
        createdReservation.setDate(LocalDate.now());
        createdReservation.setStartTime(LocalTime.now());

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
    public ReservationResponseView updateReservation(@PathVariable UUID id,
                                                     @Valid @RequestBody ReservationRequestView reservation) {
        return new ReservationResponseView();
    }

    @ApiOperation("Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseView removeReservation(@PathVariable UUID id) {
        return new ReservationResponseView();
    }
}
