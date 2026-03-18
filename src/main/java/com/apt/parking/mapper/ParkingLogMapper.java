package com.apt.parking.mapper;

import com.apt.parking.dto.request.ParkingLogGetReq;
import com.apt.parking.dto.response.ParkingLogRes;
import com.apt.parking.dto.response.ParkingLogStatsRes;
import com.apt.parking.model.ParkingLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ParkingLogMapper {

    // API-034 | 입출차 기록 INSERT
    void insertParkingLog(ParkingLog log);

    // 차량 자동 판별
    Long findVehicleIdByLicensePlate(String licensePlate);

    Long findVisitorVehicleIdByLicensePlate(String licensePlate);

    Long findFixedVisitorVehicleIdByLicensePlate(String licensePlate);

    // 차량 상세 정보
    String findVehicleInfoByVehicleId(Long vehicleId);

    String findVisitorVehicleInfoById(Long visitorVehicleId);

    String findFixedVisitorVehicleInfoById(Long fixedVisitorVehicleId);

    // API-035 | 입출차 기록 목록 조회
    List<ParkingLogRes> findParkingLogs(ParkingLogGetReq req);

    int countParkingLogs(ParkingLogGetReq req);

    // API-036 | 입출차 통계 조회
    ParkingLogStatsRes getParkingLogStats(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}