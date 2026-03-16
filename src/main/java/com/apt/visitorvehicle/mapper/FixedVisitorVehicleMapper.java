package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.model.FixedVisitorVehicle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FixedVisitorVehicleMapper {
    // API-062 | 고정 방문차량 등록
    void insertFixedVisitorVehicle(FixedVisitorVehicle fixedVisitorVehicle);
}
