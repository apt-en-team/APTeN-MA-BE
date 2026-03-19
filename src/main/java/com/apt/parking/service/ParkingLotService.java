package com.apt.parking.service;

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

    public ParkingLotRes updateParkingLot(Long lotId, ParkingLotUpdateReq request) {
        ParkingLot parkingLot = parkingLotMapper.findById(lotId);
        if (parkingLot == null) {
            throw new IllegalArgumentException("해당 주차장이 없습니다. id=" + lotId);
        }

        parkingLot.setName(request.getName());
        parkingLot.setTotalSpaces(request.getTotalSpaces());
        parkingLotMapper.update(parkingLot);

        return new ParkingLotRes(parkingLot.getLotId(), parkingLot.getName(), parkingLot.getTotalSpaces());
    }
}