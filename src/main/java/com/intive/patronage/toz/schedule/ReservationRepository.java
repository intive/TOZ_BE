package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.repository.IdentifiableRepository;

import java.util.Date;
import java.util.List;

interface ReservationRepository extends IdentifiableRepository<Reservation> {

    List<Reservation> findByStartDateBetween(Date from, Date to);
}
