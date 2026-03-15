package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationListRes {

    private Long reservationId;
    private Long userId;
    private String userName;
    private String dong;
    private String ho;
    private Long facilityId;
    private String facilityName;
    private Integer maxCapacity;
    private Long programId;        // GX만 사용
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer seatNo;
    private Integer currentCount;  // 좌석 없는 시설 현재 예약 인원
    private String  status;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;

}
