package com.apt.parking.mapper;

import com.apt.parking.dto.response.ParkingStatusRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingStatusMapper {

    // 주차 현황 조회 (전체 면수 + 현재 주차 대수 + 남은 자리)
    ParkingStatusRes getParkingStatus();
}