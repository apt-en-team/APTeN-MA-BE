package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingStatsItemRes {

    // 날짜 (daily 모드: "2026-03-19", hourly 모드: "2026-03-19")
    private String date;

    // 시간대 (hourly 모드: 0~23, daily 모드: null)
    private Integer hour;

    // 입차 건수
    private int totalIn;

    // 출차 건수
    private int totalOut;

    // 해당 시점 주차 중인 차량 수
    private int currentCount;
}