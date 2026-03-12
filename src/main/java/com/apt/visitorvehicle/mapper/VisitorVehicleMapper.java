package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.model.VisitorVehicle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitorVehicleMapper {
    // 방문차량 등록 (INSERT 후 생성된 PK를 visitorVehicleId에 세팅)
    void insertVisitorVehicle(VisitorVehicle visitorVehicle);
}