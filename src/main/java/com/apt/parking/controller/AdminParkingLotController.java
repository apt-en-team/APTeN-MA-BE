package com.apt.parking.controller;

import com.apt.common.response.ResultResponse;
import com.apt.parking.dto.request.ParkingLotUpdateReq;
import com.apt.parking.dto.response.ParkingLotRes;
import com.apt.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/parking/lots")
@RequiredArgsConstructor
public class AdminParkingLotController {

    private final ParkingLotService parkingLotService;

    @PutMapping("/{id}")
    public ResultResponse<ParkingLotRes> updateParkingLot(
            @PathVariable Long id,
            @RequestBody ParkingLotUpdateReq request) {
        return ResultResponse.success("주차장 정보 수정 성공", parkingLotService.updateParkingLot(id, request));
    }
}