package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.schedule.model.db.Reservation;
import com.intive.patronage.toz.schedule.model.view.ReservationRequestView;
import com.tngtech.java.junit.dataprovider.DataProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class ScheduleDataProvider {

    static final LocalDate VALID_LOCAL_DATE_FROM = LocalDate.parse("2017-03-01");
    static final LocalTime VALID_LOCAL_TIME = LocalTime.parse("10:00");

    static final LocalDate LOCAL_DATE_FROM = LocalDate.parse("2017-03-01");
    static final LocalDate LOCAL_DATE_TO = LocalDate.parse("2018-03-01");

    static final Date START_DATE = Date.from(LocalDate.parse("2017-03-01").atStartOfDay().toInstant(ZoneOffset.UTC));
    static final Date END_DATE = Date.from(LocalDate.parse("2017-03-02").atStartOfDay().toInstant(ZoneOffset.UTC));
    static final UUID OWNER_UUID = UUID.randomUUID();
    static final UUID MODIFICATION_AUTHOR_UUID = UUID.randomUUID();
    static final String MODIFICATION_MESSAGE = "string";
    private static final Long EXAMPLE_TIMESTAMP = 1490134074968L;

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
    public static Object[] getReservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(START_DATE);
        reservation.setEndDate(END_DATE);
        reservation.setOwnerUuid(OWNER_UUID);
        reservation.setCreationDate(new Date(EXAMPLE_TIMESTAMP));
        reservation.setModificationDate(new Date(EXAMPLE_TIMESTAMP));
        return new Reservation[]{reservation};
    }
}
