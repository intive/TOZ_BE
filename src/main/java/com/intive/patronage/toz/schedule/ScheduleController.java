package com.intive.patronage.toz.schedule;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {

    @Autowired
    public ScheduleController() {
        // TODO initialize service
    }

    @ApiOperation("Get schedule")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleView> getSchedule(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          @RequestParam("startDate") LocalDate startDate,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          @RequestParam("endDate") LocalDate endDate) {

        return new ArrayList<>();
    }

    @ApiOperation("Make reservation")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationView makeReservation(@Valid @RequestBody ReservationView reservation) {
        return new ReservationView();
    }

    @ApiOperation("Update reservation")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationView updateReservation(@PathVariable UUID id,
                                             @Valid @RequestBody ReservationView reservation) {
        return new ReservationView();
    }

    @ApiOperation("Delete reservation")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationView removeReservation(@PathVariable UUID id) {
        return new ReservationView();
    }
}
