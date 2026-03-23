package com.apt.visitorvehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.*;
import com.apt.visitorvehicle.mapper.VisitorVehicleMapper;
import com.apt.visitorvehicle.model.VisitorVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorVehicleService {
    private final VisitorVehicleMapper visitorVehicleMapper;

    // 방문차량 조회 + 본인 확인 (없으면 404, 본인 아니면 403)
    private VisitorVehicle findMyVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        if (!v.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);
        return v;
    }

    // Model → Response DTO 변환
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

    // API-030 | 방문차량 사전등록 (자동 승인)
    public VisitorVehicleRes registerVisitorVehicle(Long userId, VisitorVehicleReq req) {
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        VisitorVehicle v = new VisitorVehicle();
        v.setUserId(userId);
        v.setLicensePlate(req.getLicensePlate());
        v.setVisitorName(req.getVisitorName());
        v.setVisitPurpose(req.getVisitPurpose());
        v.setVisitDate(req.getVisitDate());
        v.setStatus("APPROVED");

        visitorVehicleMapper.insertVisitorVehicle(v);
        return toRes(v);
    }

    // API-031 | 내 방문차량 목록 조회 (필터 + 페이징 + 사이드 통계)
    public VisitorVehicleListRes getMyVisitorVehicles(Long userId, VisitorVehicleGetReq req) {
        List<VisitorVehicle> list = visitorVehicleMapper.findByUserIdWithFilter(userId, req);
        int totalCount = visitorVehicleMapper.countByUserIdWithFilter(userId, req);
        List<VisitorVehicleRes> resList = list.stream().map(this::toRes).toList();

        VisitorVehicleListRes result = new VisitorVehicleListRes();
        result.setContent(resList);
        result.setPage(req.getPage());
        result.setTotalPages((int) Math.ceil((double) totalCount / req.getSize()));
        result.setTotalCount(totalCount);
        // 사이드 카드 통계 (필터 무관)
        result.setTodayCount(visitorVehicleMapper.countTodayByUserId(userId));
        result.setUpcomingCount(visitorVehicleMapper.countUpcomingByUserId(userId));
        result.setAllCount(visitorVehicleMapper.countAllByUserId(userId));
        return result;
    }

    // API-032 | 방문차량 상세 조회
    public VisitorVehicleRes getVisitorVehicleDetail(Long userId, Long visitorVehicleId) {
        return toRes(findMyVehicle(userId, visitorVehicleId));
    }

    // API-033 | 방문차량 재사용 등록 (기존 정보 복사 + 새 날짜로 INSERT)
    public VisitorVehicleRes reuseVisitorVehicle(Long userId, Long visitorVehicleId, LocalDate visitDate) {
        VisitorVehicle original = findMyVehicle(userId, visitorVehicleId);
        if (visitDate.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        VisitorVehicle v = new VisitorVehicle();
        v.setUserId(userId);
        v.setLicensePlate(original.getLicensePlate());
        v.setVisitorName(original.getVisitorName());
        v.setVisitPurpose(original.getVisitPurpose());
        v.setVisitDate(visitDate);
        v.setStatus("APPROVED");

        visitorVehicleMapper.insertVisitorVehicle(v);
        return toRes(v);
    }

    // API-034 | 방문차량 수정
    public void updateVisitorVehicle(Long userId, Long visitorVehicleId, VisitorVehicleReq req) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        v.update(req.getLicensePlate(), req.getVisitorName(),
                req.getVisitPurpose(), req.getVisitDate());
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // API-067 | 방문차량 소프트 삭제
    public void deleteVisitorVehicle(Long userId, Long visitorVehicleId) {
        findMyVehicle(userId, visitorVehicleId);
        visitorVehicleMapper.deleteVisitorVehicle(visitorVehicleId);
    }

    // API-068 | 방문차량 등록 취소 (APPROVED → CANCELLED)
    public void cancelVisitorVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);
        if (!"APPROVED".equals(v.getStatus())) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_APPROVED);
        }
        v.cancel();
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // ==================== 관리자 ====================

    // API-069 | 관리자 방문 예정 현황 조회 (user+household JOIN, 필터 + 페이징)
    public AdminVisitorVehicleListRes getAdminVisitorVehicles(AdminVisitorVehicleGetReq req) {
        List<AdminVisitorVehicleRes> list = visitorVehicleMapper.findAdminVisitorVehicles(req);
        int totalCount = visitorVehicleMapper.countAdminVisitorVehicles(req);

        AdminVisitorVehicleListRes result = new AdminVisitorVehicleListRes();
        result.setContent(list);
        result.setPage(req.getPage());
        result.setTotalPages((int) Math.ceil((double) totalCount / req.getSize()));
        result.setTotalCount(totalCount);
        return result;
    }

    // API-070 | 관리자 방문차량 통계 (오늘/내일/이번달/전체)
    public AdminVisitorVehicleStatsRes getAdminVisitorVehicleStats() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        AdminVisitorVehicleStatsRes result = new AdminVisitorVehicleStatsRes();
        result.setTodayCount(visitorVehicleMapper.countAdminToday(today));
        result.setTomorrowCount(visitorVehicleMapper.countAdminTomorrow(tomorrow));
        result.setMonthCount(visitorVehicleMapper.countAdminThisMonth(today.getYear(), today.getMonthValue()));
        result.setTotalCount(visitorVehicleMapper.countAdminTotal());
        return result;
    }
}