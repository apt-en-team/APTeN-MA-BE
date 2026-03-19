package com.apt.parking.service;

import com.apt.parking.dto.request.ParkingLogGetReq;
import com.apt.parking.dto.request.ParkingLogReq;
import com.apt.parking.dto.response.ParkingLogListRes;
import com.apt.parking.dto.response.ParkingLogRes;
import com.apt.parking.dto.response.ParkingLogStatsRes;
import com.apt.parking.mapper.ParkingLogMapper;
import com.apt.parking.model.ParkingLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLogService {

    private final ParkingLogMapper parkingLogMapper;

    // API-034 | 입출차 기록 생성
    // 차량번호로 등록차량 → 방문차량 → 고정방문차량 순서로 자동 판별
    public ParkingLogRes createParkingLog(ParkingLogReq req) {

        ParkingLog log = new ParkingLog();
        log.setLicensePlate(req.getLicensePlate());
        log.setEntryType(req.getEntryType());

        String vehicleType;
        String vehicleInfo;

        // 1순위: 등록차량 판별 (vehicle 테이블)
        Long vehicleId = parkingLogMapper.findVehicleIdByLicensePlate(req.getLicensePlate());
        if (vehicleId != null) {
            log.setVehicleId(vehicleId);
            vehicleType = "등록차량";
            vehicleInfo = parkingLogMapper.findVehicleInfoByVehicleId(vehicleId);

        } else {
            // 2순위: 방문차량 판별 (당일 + APPROVED + is_deleted=0)
            Long visitorVehicleId = parkingLogMapper.findVisitorVehicleIdByLicensePlate(req.getLicensePlate());
            if (visitorVehicleId != null) {
                log.setVisitorVehicleId(visitorVehicleId);
                vehicleType = "방문차량";
                vehicleInfo = parkingLogMapper.findVisitorVehicleInfoById(visitorVehicleId);

            } else {
                // 3순위: 고정방문차량 판별 (기간 내 활성)
                Long fixedVisitorVehicleId = parkingLogMapper.findFixedVisitorVehicleIdByLicensePlate(req.getLicensePlate());
                if (fixedVisitorVehicleId != null) {
                    log.setFixedVisitorVehicleId(fixedVisitorVehicleId);
                    vehicleType = "고정방문차량";
                    vehicleInfo = parkingLogMapper.findFixedVisitorVehicleInfoById(fixedVisitorVehicleId);

                } else {
                    // 4순위: 미등록차량
                    vehicleType = "미등록차량";
                    vehicleInfo = null;
                }
            }
        }

        // DB에 INSERT
        parkingLogMapper.insertParkingLog(log);

        // 응답 DTO 조합
        ParkingLogRes res = new ParkingLogRes();
        res.setLogId(log.getLogId());
        res.setLicensePlate(log.getLicensePlate());
        res.setEntryType(log.getEntryType());
        res.setVehicleId(log.getVehicleId());
        res.setVisitorVehicleId(log.getVisitorVehicleId());
        res.setFixedVisitorVehicleId(log.getFixedVisitorVehicleId());
        res.setLoggedAt(LocalDateTime.now());
        res.setVehicleType(vehicleType);
        res.setVehicleInfo(vehicleInfo);
        return res;
    }

    // API-035 | 입출차 기록 목록 조회 (필터 + 페이징)
    public ParkingLogListRes getParkingLogs(ParkingLogGetReq req) {
        List<ParkingLogRes> list = parkingLogMapper.findParkingLogs(req);
        int totalCount = parkingLogMapper.countParkingLogs(req);

        ParkingLogListRes result = new ParkingLogListRes();
        result.setContent(list);
        result.setPage(req.getPage());
        result.setTotalPages((int) Math.ceil((double) totalCount / req.getSize()));
        result.setTotalCount(totalCount);
        return result;
    }

    // API-036 | 입출차 통계 조회
    // date: 오늘 기준 (todayIn, todayOut, monthTotal 계산용)
    // startDate/endDate: 필터 범위 (unregistered 계산용)
    public ParkingLogStatsRes getParkingLogStats(LocalDate startDate, LocalDate endDate) {
        // 필터 없으면 이번 달 기준
        if (startDate == null && endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now();
        }
        return parkingLogMapper.getParkingLogStats(startDate, endDate);
    }
}