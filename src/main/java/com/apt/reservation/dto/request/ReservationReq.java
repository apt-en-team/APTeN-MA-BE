package com.apt.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationReq {

    @NotNull
    private Long facilityId;           // 시설 ID
    @NotNull
    private LocalDate reservationDate; // 예약 날짜 (2026-03-15)
    @NotNull
    private LocalTime startTime;       // 시작 시간 (10:00)
    @NotNull
    private LocalTime endTime;         // 종료 시간 (11:00)
    private Integer seatNo;            // 좌석 번호 (독서실·스크린룸만, 나머지 null)

}
