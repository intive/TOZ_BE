package com.intive.patronage.toz.schedule.repository;

import com.intive.patronage.toz.repository.IdentifiableRepository;
import com.intive.patronage.toz.schedule.model.db.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends IdentifiableRepository<Reservation> {

    List<Reservation> findByStartDateBetween(Date from, Date to);
}
