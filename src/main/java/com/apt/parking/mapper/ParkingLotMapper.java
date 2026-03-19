package com.apt.parking.mapper;

import com.apt.parking.model.ParkingLot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingLotMapper {
    ParkingLot findById(Long lotId);
    int update(ParkingLot parkingLot);
}