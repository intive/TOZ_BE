package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.error.model.ArgumentErrorResponse;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.schedule.constant.LocalDateTimeFormat;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.view.DayConfigView;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.schedule.model.view.ScheduleView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import com.intive.patronage.toz.users.UserRepository;
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
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.schedule.util.DateUtil.convertToDate;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
class ScheduleController {

    private final ScheduleParser scheduleParser;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    @Value("${timezoneOffset}")
    private String zoneOffset = "Z";

    @Autowired
    ScheduleController(ScheduleService scheduleService, ScheduleParser scheduleParser, UserRepository userRepository) {
        this.scheduleService = scheduleService;
        this.scheduleParser = scheduleParser;
        this.userRepository = userRepository;
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

        List<ScheduleReservation> scheduleReservations = scheduleService.findScheduleReservations(from, to);
        List<ReservationResponseView> reservationResponseViews = new ArrayList<ReservationResponseView>();
        for (ScheduleReservation scheduleReservation : scheduleReservations) {
            reservationResponseViews.add(convertToReservationResponseView(scheduleReservation));
        }
        List<DayConfigView> dayConfigs = scheduleParser.getDaysConfig();
        return new ScheduleView(reservationResponseViews, dayConfigs);

    }

    @ApiOperation("Get single reservation by id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ArgumentErrorResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseView getReservation(@PathVariable UUID id) {
        return convertToReservationResponseView(scheduleService.findReservation(id));
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
        //TODO: wait for security users auth to get author id from java.secuity.Principal
        //UUID modificationAuthorId = userRepository.findByEmail(principal.getName()).getId();
        ScheduleReservation createdScheduleReservation = convertToReservation(reservationRequestView);
        ReservationResponseView createdReservationResponseView = convertToReservationResponseView(
                scheduleService.makeReservation(createdScheduleReservation, reservationRequestView.getModificationMessage()));
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
        //TODO: wait for security users auth to get author id from java.secuity.Principal
        //UUID modificationAuthorId = userRepository.findByEmail(principal.getName()).getId();
        ScheduleReservation updatedScheduleReservation = convertToReservation(reservationRequestView);
        ReservationResponseView updatedReservationResponseView = convertToReservationResponseView(
                scheduleService.updateReservation(id, updatedScheduleReservation, reservationRequestView.getModificationMessage()));
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
        return convertToReservationResponseView(scheduleService.removeReservation(id));
    }

    private ScheduleReservation convertToReservation(ReservationRequestView reservationRequestView) {
        ScheduleReservation scheduleReservation = new ScheduleReservation();
        scheduleReservation.setStartDate(
                convertToDate(
                        reservationRequestView.getDate(),
                        reservationRequestView.getStartTime(),
                        ZoneOffset.of(zoneOffset)));
        scheduleReservation.setEndDate(
                convertToDate(
                        reservationRequestView.getDate(),
                        reservationRequestView.getEndTime(),
                        ZoneOffset.of(zoneOffset)));
        scheduleReservation.setOwnerUuid(reservationRequestView.getOwnerId());
        return scheduleReservation;
    }

    private ReservationResponseView convertToReservationResponseView(ScheduleReservation scheduleReservation) {
        ReservationResponseView reservationResponseView = new ReservationResponseView();
        reservationResponseView.setDate(scheduleReservation.getStartDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalDate());
        reservationResponseView.setStartTime(scheduleReservation.getStartDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalTime());
        reservationResponseView.setEndTime(scheduleReservation.getEndDate().toInstant().atOffset(ZoneOffset.of(zoneOffset)).toLocalTime());
        reservationResponseView.setOwnerId(scheduleReservation
                .getOwnerUuid());
        reservationResponseView.setCreated(scheduleReservation
                .getCreationDate()
                .getTime());
        reservationResponseView.setLastModified(scheduleReservation
                .getModificationDate()
                .getTime());
        reservationResponseView.setId(scheduleReservation.getId());
        return reservationResponseView;
    }

    private URI createLocationPath(UUID id) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String location = String.format("%s/%s", baseLocation, id);
        return UriComponentsBuilder.fromUriString(location)
                .build().toUri();
    }

    private String createStringFromLocalTime(LocalTime localTime) {
        return localTime.format(LocalDateTimeFormat.getInstanceOfLocalTimeFormatter());
    }

    private String createStringFromLocalDate(LocalDate localDate) {
        return localDate.format(LocalDateTimeFormat.getInstanceOfLocalDateFormatter());
    }
}
