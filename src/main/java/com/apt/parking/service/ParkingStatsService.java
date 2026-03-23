package com.apt.parking.service;

import com.apt.parking.dto.request.ParkingStatsReq;
import com.apt.parking.dto.response.ParkingStatsItemRes;
import com.apt.parking.dto.response.ParkingStatsRes;
import com.apt.parking.mapper.ParkingStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingStatsService {

    private final ParkingStatsMapper parkingStatsMapper;

    // API-037 | 주차 통계 조회
    // type이 "hourly"면 시간대별, 그 외(daily)면 일별 집계
    public ParkingStatsRes getParkingStats(ParkingStatsReq req) {
        List<ParkingStatsItemRes> statistics;

        if ("hourly".equals(req.getType())) {
            statistics = parkingStatsMapper.getHourlyStats(req);
        } else {
            statistics = parkingStatsMapper.getDailyStats(req);
        }

        return new ParkingStatsRes(statistics);
    }
}