package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.schedule.model.db.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends IdentifiableRepository<Reservation> {

    List<Reservation> findByStartDateBetween(Date from, Date to);
    List<Reservation> findByStartDate(Date startDate);
}
