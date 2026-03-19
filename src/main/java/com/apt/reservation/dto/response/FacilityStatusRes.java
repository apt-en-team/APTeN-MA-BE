package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class FacilityStatusRes {
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer seatNo;
    private String userName;
    private String dong;
    private String ho;
    private String status;
    private Long facilityId;
    private String facilityName;
    private Long reservationId;
    private Long userId;
}

