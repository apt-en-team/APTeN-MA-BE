package com.apt.parking.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ParkingStatsReq {

    // 조회 시작일 (없으면 전체 기준)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    // 조회 종료일 (없으면 전체 기준)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    // 집계 타입: "hourly" (시간대별) | "daily" (일별)
    private String type;
}