package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class GxApprovalRes {

    private Long      facilityId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private int       confirmedCount;
    private int       cancelledCount;
    private String    result;         // "SUCCESS"

}
