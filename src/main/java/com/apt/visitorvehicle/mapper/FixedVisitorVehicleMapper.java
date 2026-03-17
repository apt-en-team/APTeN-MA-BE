package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleRes;
import com.apt.visitorvehicle.model.FixedVisitorVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FixedVisitorVehicleMapper {
    // API-062 | 고정 방문차량 등록
    void insertFixedVisitorVehicle(FixedVisitorVehicle fixedVisitorVehicle);

    List<FixedVisitorVehicleRes> findByMyFixed(@Param("userId") Long userId,
                                               @Param("vehicleNumber") String vehicleNumber,
                                               @Param("startIdx") int startIdx,
                                               @Param("size") int size);

    FixedVisitorVehicle findFixedById(Long fixedId);

    void deleteFixedVisitorVehicle(Long fixedId);


    int countByMyFixed(@Param("userId") Long userId,
                       @Param("vehicleNumber") String vehicleNumber);

    int countUnlimitedByUserId(@Param("userId") Long userId);

    int countAllByUserId(@Param("userId") Long userId);
}
