package com.apt.visitorvehicle.mapper;

import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.response.AdminVisitorVehicleRes;
import com.apt.visitorvehicle.model.VisitorVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/*
 * 방문차량 MyBatis 매퍼 인터페이스
 * - 입주민 방문차량 CRUD 및 관리자 조회/통계 쿼리 정의
 */
@Mapper
public interface VisitorVehicleMapper {

    // ==================== 입주민 ====================

    /** API-030 | 방문차량 등록 - 자동 승인(APPROVED) 상태로 INSERT */
    void insertVisitorVehicle(VisitorVehicle visitorVehicle);

    /** API-031 | 내 방문차량 목록 조회 - 차량번호/방문자/날짜 필터 + 페이징 */
    List<VisitorVehicle> findByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);

    /** API-031 | 내 방문차량 총 개수 - 페이징 totalPages 계산용 */
    int countByUserIdWithFilter(@Param("userId") Long userId, @Param("req") VisitorVehicleGetReq req);

    /** API-032 | 방문차량 상세 조회 - PK로 단건 조회 */
    VisitorVehicle findById(Long visitorVehicleId);

    /** API-034, API-068 | 방문차량 수정 및 취소 - 수정/취소(CANCELLED) 상태 변경 공용 */
    void updateVisitorVehicle(VisitorVehicle visitorVehicle);

    /** API-067 | 방문차량 소프트 삭제 - is_deleted=1, deleted_at 기록 */
    void deleteVisitorVehicle(Long visitorVehicleId);

    /** 오늘 방문 차량 수 - visit_date = 오늘, APPROVED 상태만 */
    int countTodayByUserId(Long userId);

    /** 예정 방문 차량 수 - visit_date > 오늘, APPROVED 상태만 */
    int countUpcomingByUserId(Long userId);

    /** 전체 등록 건수 - 필터 무관, 소프트 삭제 제외 */
    int countAllByUserId(Long userId);

    // ==================== 관리자 ====================

    /** API-069 | 관리자 방문차량 목록 조회 - 날짜/차량번호 필터 + 페이징 */
    List<AdminVisitorVehicleRes> findAdminVisitorVehicles(AdminVisitorVehicleGetReq req);

    /** API-069 | 관리자 방문차량 총 건수 - 페이징 totalPages 계산용 */
    int countAdminVisitorVehicles(AdminVisitorVehicleGetReq req);

    /** 오늘 방문 예정 건수 - Java LocalDate 기준 (UTC 오차 방지) */
    int countAdminToday(@Param("today") LocalDate today);

    /** 내일 방문 예정 건수 - Java LocalDate 기준 (UTC 오차 방지) */
    int countAdminTomorrow(@Param("tomorrow") LocalDate tomorrow);

    /** 이번 달 방문 건수 - 연/월 기준 집계 */
    int countAdminThisMonth(@Param("year") int year, @Param("month") int month);

    /** 전체 등록 건수 - 소프트 삭제 제외 전체 누적 */
    int countAdminTotal();
}