package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleRes;
import com.apt.visitorvehicle.model.FixedVisitorVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FixedVisitorVehicleMapper {

    // 입주민 고정 방문 차량 등록
    void insertFixedVisitorVehicle(FixedVisitorVehicle fixedVisitorVehicle);

    // 내 고정 방문차량 목록 조회
    List<FixedVisitorVehicleRes> findByMyFixed(@Param("userId") Long userId,
                                               @Param("vehicleNumber") String vehicleNumber,
                                               @Param("startIdx") int startIdx,
                                               @Param("size") int size);

    // 고정 방문 소프트 딜리트 삭제
    FixedVisitorVehicle findFixedById(Long fixedId);

    void deleteFixedVisitorVehicle(Long fixedId);

    // 입주민 사이드 통계
    int countByMyFixed(@Param("userId") Long userId,
                       @Param("vehicleNumber") String vehicleNumber);

    int countUnlimitedByUserId(@Param("userId") Long userId);

    int countAllByUserId(@Param("userId") Long userId);

    // 관리자 전체 고정 방문차량 목록 조회
    List<FixedVisitorVehicleRes> findAdminFixedVehicles(@Param("vehicleNumber") String vehicleNumber,
                                                        @Param("dong") String dong,
                                                        @Param("startIdx") int startIdx,
                                                        @Param("size") int size);

    int countAdminFixedVehicles(@Param("vehicleNumber") String vehicleNumber,
                                @Param("dong") String dong);

    // 관리자 통계
    int countAdminFixedTotal();

    int countAdminFixedUnlimited();

    int countAdminFixedThisMonth(@Param("year") int year, @Param("month") int month);

    int countAdminFixedActive(@Param("today") java.time.LocalDate today);
}
