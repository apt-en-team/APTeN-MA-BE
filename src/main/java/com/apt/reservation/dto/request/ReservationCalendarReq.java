package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationCalendarReq {
    private Long typeId;
    private Long facilityId;

    private LocalDate startDate;
    private LocalDate endDate;
}
