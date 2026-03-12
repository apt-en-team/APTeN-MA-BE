package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.model.VisitorVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VisitorVehicleMapper {
    // 방문차량 등록
    void insertVisitorVehicle(VisitorVehicle visitorVehicle);
    // 내 방문차량 목록 조회 (필터 + 페이징)
    List<VisitorVehicle> findByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);
    // 내 방문차량 총 개수 (페이징 계산용)
    int countByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);
}