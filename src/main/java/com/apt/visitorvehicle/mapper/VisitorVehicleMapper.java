package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.response.AdminVisitorVehicleRes;
import com.apt.visitorvehicle.model.VisitorVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VisitorVehicleMapper {

    // API-030 | 방문차량 등록
    void insertVisitorVehicle(VisitorVehicle visitorVehicle);

    // API-031 | 내 방문차량 목록 조회 (필터 + 페이징)
    List<VisitorVehicle> findByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);

    // API-031 | 내 방문차량 총 개수 (페이징 계산용)
    int countByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);

    // API-032 | 방문차량 상세 조회 (PK로 조회)
    VisitorVehicle findById(Long visitorVehicleId);

    // API-034 | 방문차량 수정 (cancel 상태 변경도 공용)
    void updateVisitorVehicle(VisitorVehicle visitorVehicle);

    // API-035 | 방문차량 삭제
    void deleteVisitorVehicle(Long visitorVehicleId);

    // 사이드 카드용 | 오늘 방문 차량 수 (visit_date = 오늘, APPROVED만)
    int countTodayByUserId(Long userId);

    // 사이드 카드용 | 예비 방문 차량 수 (visit_date > 오늘, APPROVED만)
    int countUpcomingByUserId(Long userId);

    // 전체 등록 건수 (필터 무관)
    int countAllByUserId(Long userId);

    /** API-069 | 관리자 방문차량 목록 조회 (페이징) */
    List<AdminVisitorVehicleRes> findAdminVisitorVehicles(AdminVisitorVehicleGetReq req);

    /** API-069 | 관리자 방문차량 총 건수 */
    int countAdminVisitorVehicles(AdminVisitorVehicleGetReq req);
}