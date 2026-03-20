package com.apt.parking.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.parking.dto.request.ParkingLotUpdateReq;
import com.apt.parking.dto.response.ParkingLotRes;
import com.apt.parking.mapper.ParkingLotMapper;
import com.apt.parking.model.ParkingLot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotMapper parkingLotMapper;

    // 주차장 정보 수정 (name, total_spaces)
    // current_count는 parking_log 집계로 계산하므로 수동 입력 불가
    public ParkingLotRes updateParkingLot(Long lotId, ParkingLotUpdateReq request) {

        // 주차장 존재 여부 확인
        ParkingLot parkingLot = parkingLotMapper.findById(lotId);
        if (parkingLot == null) {
            throw new CustomException(ErrorCode.PARKING_LOT_NOT_FOUND);
        }

        // 이름과 전체 주차면 수만 수정
        parkingLot.setName(request.getName());
        parkingLot.setTotalSpaces(request.getTotalSpaces());
        parkingLotMapper.update(parkingLot);

        return new ParkingLotRes(parkingLot.getLotId(), parkingLot.getName(), parkingLot.getTotalSpaces());
    }
}