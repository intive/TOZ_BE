package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.intive.patronage.toz.schedule.model.view.ReservationResponseView;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.enumerations.Role;
import com.tngtech.java.junit.dataprovider.DataProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class ScheduleDataProvider {

    static final LocalDate VALID_LOCAL_DATE_FROM = LocalDate.parse("2017-03-01");
    static final LocalDate VALID_LOCAL_DATE_TO = LocalDate.parse("2018-03-01");
    static final LocalTime VALID_LOCAL_TIME = LocalTime.parse("10:00");
    static final String MODIFICATION_MESSAGE = "string";
    static final Date VALID_DATE_FROM = Date.from(LocalDate.parse("2017-03-01").atStartOfDay().toInstant(ZoneOffset.UTC));
    private static final Date VALID_DATE_TO = Date.from(LocalDate.parse("2017-03-02").atStartOfDay().toInstant(ZoneOffset.UTC));
    private static final UUID OWNER_UUID = UUID.randomUUID();
    private static final Long EXAMPLE_TIMESTAMP = 1490134074968L;
    private static final String EXAMPLE_NAME = "name";
    static final User EXAMPLE_USER = new User(null, null,EXAMPLE_NAME, EXAMPLE_NAME, Role.VOLUNTEER);

    @DataProvider
    public static Object[] getReservationRequestView() {
        ReservationRequestView view = new ReservationRequestView();
        view.setDate(VALID_LOCAL_DATE_FROM);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setEndTime(VALID_LOCAL_TIME);
        view.setOwnerId(UUID.randomUUID());
        view.setModificationMessage("string");
        return new ReservationRequestView[]{view};
    }

    @DataProvider
    public static Object[] getReservationResponseView() {
        ReservationResponseView view = new ReservationResponseView();
        view.setDate(VALID_LOCAL_DATE_FROM);
        view.setStartTime(VALID_LOCAL_TIME);
        view.setEndTime(VALID_LOCAL_TIME);
        view.setOwnerId(UUID.randomUUID());
        view.setOwnerSurname("string");
        view.setOwnerForename("string");
        view.setCreated(EXAMPLE_TIMESTAMP);
        view.setLastModified(EXAMPLE_TIMESTAMP);
        return new ReservationResponseView[]{view};
    }

    @DataProvider
    public static Object[] getReservation() {
        ScheduleReservation scheduleReservation = new ScheduleReservation();
        scheduleReservation.setStartDate(VALID_DATE_FROM);
        scheduleReservation.setEndDate(VALID_DATE_TO);
        scheduleReservation.setOwnerUuid(OWNER_UUID);
        scheduleReservation.setCreationDate(new Date(EXAMPLE_TIMESTAMP));
        scheduleReservation.setModificationDate(new Date(EXAMPLE_TIMESTAMP));
        return new ScheduleReservation[]{scheduleReservation};
    }
}
