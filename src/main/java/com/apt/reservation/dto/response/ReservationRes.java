package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationRes {

    private Long reservationId;
    private Long userId;
    private Long facilityId;
    private Long programId; // GX만
    private String facilityName; // JOIN
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer seatNo; // 좌석 없는 시설은 null
    private String status;
    private LocalDateTime createdAt;
    private Integer currentCount; // 일반 시설 확정 인원
    private Integer pendingCount; // GX 대기 인원
    private LocalDateTime approvedAt;
    private LocalDateTime cancelledAt;
    private Long maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userName;
    private String dong;
    private String ho;
    private Long typeId;
    private String typeName;



}
