package com.apt.visitorvehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.AdminVisitorVehicleRes;
import com.apt.visitorvehicle.dto.response.VisitorVehicleRes;
import com.apt.visitorvehicle.mapper.VisitorVehicleMapper;
import com.apt.visitorvehicle.model.VisitorVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 방문차량 서비스
 * - 입주민 방문차량 CRUD 비즈니스 로직 처리
 * - 관리자 방문차량 목록 조회 및 통계 집계
 */
@Service
@RequiredArgsConstructor
public class VisitorVehicleService {

    private final VisitorVehicleMapper visitorVehicleMapper;

    /*
     * 방문차량 단건 조회 + 본인 확인
     * - 존재하지 않으면 404 (VISITOR_VEHICLE_NOT_FOUND)
     * - 본인 등록이 아니면 403 (FORBIDDEN)
     */
    private VisitorVehicle findMyVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
        if (!v.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return v;
    }

    /*
     * VisitorVehicle Model → VisitorVehicleRes DTO 변환
     * - createdAt이 null인 경우 현재 시각으로 대체
     */
    private VisitorVehicleRes toRes(VisitorVehicle v) {
        VisitorVehicleRes res = new VisitorVehicleRes();
        res.setVisitorVehicleId(v.getVisitorVehicleId());
        res.setUserId(v.getUserId());
        res.setLicensePlate(v.getLicensePlate());
        res.setVisitorName(v.getVisitorName());
        res.setVisitDate(v.getVisitDate());
        res.setVisitPurpose(v.getVisitPurpose());
        res.setStatus(v.getStatus());
        res.setCreatedAt(v.getCreatedAt() != null ? v.getCreatedAt() : LocalDateTime.now());
        return res;
    }

    // ==================== 입주민 ====================

    /*
     * API-030 | 방문차량 사전등록
     * - 과거 날짜 선택 시 400 (PAST_VISIT_DATE)
     * - 등록 즉시 APPROVED 상태로 자동 승인
     */
    public VisitorVehicleRes registerVisitorVehicle(Long userId, VisitorVehicleReq req) {
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }

        VisitorVehicle visitorVehicle = new VisitorVehicle();
        visitorVehicle.setUserId(userId);
        visitorVehicle.setLicensePlate(req.getLicensePlate());
        visitorVehicle.setVisitorName(req.getVisitorName());
        visitorVehicle.setVisitPurpose(req.getVisitPurpose());
        visitorVehicle.setVisitDate(req.getVisitDate());
        visitorVehicle.setStatus("APPROVED");

        visitorVehicleMapper.insertVisitorVehicle(visitorVehicle);
        return toRes(visitorVehicle);
    }

    /*
     * API-031 | 내 방문차량 목록 조회
     * - 차량번호/방문자/날짜 필터 + 페이징
     * - 사이드 카드용 통계(오늘/예정/전체)를 필터 무관하게 함께 반환
     */
    public Map<String, Object> getMyVisitorVehicles(Long userId, VisitorVehicleGetReq req) {
        List<VisitorVehicle> list = visitorVehicleMapper.findByUserIdWithFilter(userId, req);
        int totalCount = visitorVehicleMapper.countByUserIdWithFilter(userId, req);
        List<VisitorVehicleRes> resList = list.stream().map(this::toRes).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("content", resList);
        result.put("page", req.getPage());
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));
        result.put("totalCount", totalCount);
        // 사이드 카드용 통계 (필터 무관하게 항상 집계)
        result.put("todayCount", visitorVehicleMapper.countTodayByUserId(userId));
        result.put("upcomingCount", visitorVehicleMapper.countUpcomingByUserId(userId));
        result.put("allCount", visitorVehicleMapper.countAllByUserId(userId));
        return result;
    }

    /*
     * API-032 | 방문차량 상세 조회
     * - 본인 등록 차량만 조회 가능 (findMyVehicle에서 403/404 처리)
     */
    public VisitorVehicleRes getVisitorVehicleDetail(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);
        return toRes(v);
    }

    /*
     * API-033 | 방문차량 재사용 등록
     * - 기존 차량 정보(차량번호/방문자/목적)를 그대로 복사하여 새 날짜로 재등록
     * - 과거 날짜 선택 시 400 (PAST_VISIT_DATE)
     * - 재등록 즉시 APPROVED 상태로 자동 승인
     */
    public VisitorVehicleRes reuseVisitorVehicle(Long userId, Long visitorVehicleId, LocalDate visitDate) {
        VisitorVehicle original = findMyVehicle(userId, visitorVehicleId);

        if (visitDate.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }

        VisitorVehicle newVehicle = new VisitorVehicle();
        newVehicle.setUserId(userId);
        newVehicle.setLicensePlate(original.getLicensePlate());
        newVehicle.setVisitorName(original.getVisitorName());
        newVehicle.setVisitPurpose(original.getVisitPurpose());
        newVehicle.setVisitDate(visitDate);
        newVehicle.setStatus("APPROVED");

        visitorVehicleMapper.insertVisitorVehicle(newVehicle);
        return toRes(newVehicle);
    }

    /**
     * API-034 | 방문차량 수정
     * - APPROVED 상태에서만 수정 가능
     * - 과거 날짜 선택 시 400 (PAST_VISIT_DATE)
     */
    public void updateVisitorVehicle(Long userId, Long visitorVehicleId, VisitorVehicleReq req) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);

        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }

        v.update(req.getLicensePlate(), req.getVisitorName(),
                req.getVisitPurpose(), req.getVisitDate());
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    /*
     * API-067 | 방문차량 소프트 삭제
     * - 물리 삭제가 아닌 is_deleted=1, deleted_at 기록으로 이력 보존
     */
    public void deleteVisitorVehicle(Long userId, Long visitorVehicleId) {
        findMyVehicle(userId, visitorVehicleId);
        visitorVehicleMapper.deleteVisitorVehicle(visitorVehicleId);
    }

    /*
     * API-068 | 방문차량 등록 취소
     * - APPROVED 상태에서만 취소 가능, 이미 취소된 경우 400 (VISITOR_VEHICLE_NOT_APPROVED)
     * - 상태를 CANCELLED로 변경
     */
    public void cancelVisitorVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);

        if (!"APPROVED".equals(v.getStatus())) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_APPROVED);
        }

        v.cancel();
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // ==================== 관리자 ====================

    /*
     * API-069 | 관리자 방문 예정 현황 조회
     * - 날짜/차량번호 필터 + 페이징
     * - user/household JOIN으로 등록자 이름, 동/호 정보 포함
     */
    public Map<String, Object> getAdminVisitorVehicles(AdminVisitorVehicleGetReq req) {
        List<AdminVisitorVehicleRes> list = visitorVehicleMapper.findAdminVisitorVehicles(req);
        int totalCount = visitorVehicleMapper.countAdminVisitorVehicles(req);

        Map<String, Object> result = new HashMap<>();
        result.put("page", req.getPage());
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));
        result.put("content", list);
        result.put("totalCount", totalCount);
        return result;
    }

    /*
     * API-070 | 관리자 방문차량 통계 조회
     * - CURDATE() 대신 Java LocalDate.now() 사용 (JVM 타임존 기준, UTC 오차 방지)
     * - 오늘/내일/이번달/전체 건수 집계
     */
    public Map<String, Object> getAdminVisitorVehicleStats() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Map<String, Object> result = new HashMap<>();
        result.put("todayCount",    visitorVehicleMapper.countAdminToday(today));
        result.put("tomorrowCount", visitorVehicleMapper.countAdminTomorrow(tomorrow));
        result.put("monthCount",    visitorVehicleMapper.countAdminThisMonth(today.getYear(), today.getMonthValue()));
        result.put("totalCount",    visitorVehicleMapper.countAdminTotal());
        return result;
    }
}