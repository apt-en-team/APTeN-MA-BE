package com.apt.parking.controller;

import com.apt.common.response.ResultResponse;
import com.apt.parking.dto.request.ParkingLogGetReq;
import com.apt.parking.dto.request.ParkingLogReq;
import com.apt.parking.dto.response.ParkingLogListRes;
import com.apt.parking.dto.response.ParkingLogRes;
import com.apt.parking.dto.response.ParkingLogStatsRes;
import com.apt.parking.service.ParkingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/parking/logs")
@RequiredArgsConstructor
public class ParkingLogController {

    private final ParkingLogService parkingLogService;

    // API-034 | 입출차 기록 생성 (POST /api/parking/logs)
    @PostMapping
    public ResultResponse<ParkingLogRes> createParkingLog(
            @RequestBody ParkingLogReq req) {

        return ResultResponse.success("기록 생성 성공", parkingLogService.createParkingLog(req));
    }

    // API-035 | 입출차 기록 목록 조회 (GET /api/parking/logs)
    @GetMapping
    public ResultResponse<ParkingLogListRes> getParkingLogs(
            @ModelAttribute ParkingLogGetReq req) {

        return ResultResponse.success("기록 조회 성공", parkingLogService.getParkingLogs(req));
    }

    // API-036 | 입출차 통계 조회 (GET /api/parking/logs/stats)
    // ?start_date=2026-03-01&end_date=2026-03-18 (선택, 없으면 전체 기준)
    @GetMapping("/stats")
    public ResultResponse<ParkingLogStatsRes> getParkingLogStats(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResultResponse.success("통계 조회 성공", parkingLogService.getParkingLogStats(startDate, endDate));
    }
}