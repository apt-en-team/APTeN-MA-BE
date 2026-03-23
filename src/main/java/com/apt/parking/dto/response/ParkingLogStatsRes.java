package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

// API-036 | 입출차 통계 응답 DTO
@Getter
@Setter
public class ParkingLogStatsRes {
    private int todayIn;       // 오늘 입차 건수
    private int todayOut;      // 오늘 출차 건수
    private int unregistered;  // 미등록 차량 건수 (필터 기간 기준)
    private int monthTotal;    // 이번 달 전체 건수
}