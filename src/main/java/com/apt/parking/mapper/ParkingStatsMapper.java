package com.apt.parking.mapper;

import com.apt.parking.dto.request.ParkingStatsReq;
import com.apt.parking.dto.response.ParkingStatsItemRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkingStatsMapper {

    // 시간대별 통계 (hourly): 날짜+시간 기준 입/출차 건수 집계
    List<ParkingStatsItemRes> getHourlyStats(ParkingStatsReq req);

    // 일별 통계 (daily): 날짜 기준 입/출차 건수 집계
    List<ParkingStatsItemRes> getDailyStats(ParkingStatsReq req);
}