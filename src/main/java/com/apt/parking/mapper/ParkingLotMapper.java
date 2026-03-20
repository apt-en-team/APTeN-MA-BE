package com.apt.parking.mapper;

import com.apt.parking.model.ParkingLot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingLotMapper {

    // 주차장 ID로 주차장 단건 조회
    ParkingLot findById(Long lotId);

    // 주차장 정보 수정 (name, total_spaces)
    int update(ParkingLot parkingLot);
}