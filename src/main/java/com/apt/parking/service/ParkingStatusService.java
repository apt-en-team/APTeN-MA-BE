package com.apt.parking.service;

import com.apt.parking.dto.response.ParkingStatusRes;
import com.apt.parking.mapper.ParkingStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingStatusService {

    private final ParkingStatusMapper parkingStatusMapper;

    // 주차 현황 조회
    // current_count = IN 건수 - OUT 건수
    // available_count = total_spaces - current_count
    public ParkingStatusRes getParkingStatus() {
        return parkingStatusMapper.getParkingStatus();
    }
}