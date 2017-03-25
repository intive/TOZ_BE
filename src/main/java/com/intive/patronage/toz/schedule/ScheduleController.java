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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {

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

        return new ScheduleView();
    }

    @ApiOperation("Make reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationView makeReservation(@Valid @RequestBody ReservationView reservation) {
        // todo response entity with location
        return new ReservationView();
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
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationView removeReservation(@PathVariable UUID id) {
        return new ReservationView();
    }
}
