package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SeatStatusReq {
    private Long facilityId;
    private LocalDate reservationDate;
    private LocalTime startTime;
}
