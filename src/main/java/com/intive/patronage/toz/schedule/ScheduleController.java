package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.model.ArgumentErrorResponse;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.schedule.model.view.DayConfigView;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.schedule.model.view.ScheduleView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import java.util.List;
import java.util.UUID;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
class ScheduleController {

    private final ScheduleParser scheduleParser;
    private final ScheduleService scheduleService;
    @Value("${timezoneOffset}")
    private String zoneOffset = "Z";

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

        List<ReservationResponseView> reservationViews = scheduleService.findScheduleReservations(from, to);
        List<DayConfigView> dayConfigs = scheduleParser.getDaysConfig();
        return new ScheduleView(reservationViews, dayConfigs);

    }

    @Autowired
    ScheduleController(ScheduleService scheduleService, ScheduleParser scheduleParser) {
        this.scheduleService = scheduleService;
        this.scheduleParser = scheduleParser;
    }

    @ApiOperation("Get single reservation by id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseView getReservation(@PathVariable UUID id) {
        return scheduleService.findReservation(id);
    }

    @ApiOperation("Make reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "Already exists", response = ErrorResponse.class),
            @ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationResponseView> makeReservation(@Valid @RequestBody ReservationRequestView reservationRequestView) {
        ReservationResponseView createdReservationResponseView =
                scheduleService.makeReservation(reservationRequestView);
        URI location = createLocationPath(createdReservationResponseView.getId());
        return ResponseEntity.created(location)
                .body(createdReservationResponseView);
    }

    @ApiOperation("Update reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationResponseView> updateReservation(@PathVariable UUID id,
                                                                     @Valid @RequestBody ReservationRequestView reservationRequestView) {
        ReservationResponseView updatedReservationResponseView =
                scheduleService.updateReservation(id, reservationRequestView);
        URI location = createLocationPath(id);
        return ResponseEntity.created(location)
                .body(updatedReservationResponseView);
    }

    @ApiOperation("Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseView removeReservation(@PathVariable UUID id) {
        return scheduleService.removeReservation(id);
    }


    private URI createLocationPath(UUID id) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String location = String.format("%s/%s", baseLocation, id);
        return UriComponentsBuilder.fromUriString(location)
                .build().toUri();
    }
}
