package com.apt.parking.controller;

import com.apt.common.response.ResultResponse;
import com.apt.parking.dto.response.ParkingStatusRes;
import com.apt.parking.service.ParkingStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingStatusController {

    private final ParkingStatusService parkingStatusService;

    // 주차 현황 조회 (GET /api/parking/status)
    // total_spaces: 전체 주차 면수
    // current_count: 현재 주차 대수 (IN - OUT)
    // available_count: 남은 자리
    @GetMapping("/status")
    public ResultResponse<ParkingStatusRes> getParkingStatus() {
        return ResultResponse.success("주차 현황 조회 성공", parkingStatusService.getParkingStatus());
    }
}