package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationRes {

    private Long          reservationId;
    private Long          userId;
    private Long          facilityId;
    private String        facilityName;    // JOIN
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime     endTime;
    private Integer       seatNo;          // 좌석 없는 시설은 null
    private String        status;
    private LocalDateTime createdAt;

}
