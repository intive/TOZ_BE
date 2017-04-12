package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.schedule.model.db.ScheduleReservation;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends IdentifiableRepository<ScheduleReservation> {

    List<ScheduleReservation> findByStartDateBetween(Date from, Date to);
    List<ScheduleReservation> findByStartDate(Date startDate);
}
