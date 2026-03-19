package com.apt.parking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ParkingStatsRes {

    // 프론트 chartSeries 가공에 사용되는 통계 배열
    private List<ParkingStatsItemRes> statistics;
}