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

@Service
@RequiredArgsConstructor
public class VisitorVehicleService {
    private final VisitorVehicleMapper visitorVehicleMapper;

    // 방문차량 조회 + 본인 확인 (없으면 404, 본인 아니면 403)
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

    // 방문차량 사전등록 (API-030)
    public VisitorVehicleRes registerVisitorVehicle(Long userId, VisitorVehicleReq req) {
        // 과거 날짜 검증
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

    // 내 방문차량 목록 조회 (API-031)
    public Map<String, Object> getMyVisitorVehicles(Long userId, VisitorVehicleGetReq req) {
        List<VisitorVehicle> list = visitorVehicleMapper.findByUserIdWithFilter(userId, req);
        int totalCount = visitorVehicleMapper.countByUserIdWithFilter(userId, req);
        List<VisitorVehicleRes> resList = list.stream().map(this::toRes).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("content", resList);
        result.put("page", req.getPage());
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));
        result.put("totalCount", totalCount);
        // 사이드 카드용 통계 (필터 무관)
        result.put("todayCount", visitorVehicleMapper.countTodayByUserId(userId));
        result.put("upcomingCount", visitorVehicleMapper.countUpcomingByUserId(userId));
        result.put("allCount", visitorVehicleMapper.countAllByUserId(userId));
        return result;
    }

    // 방문차량 상세 조회 (API-032)
    public VisitorVehicleRes getVisitorVehicleDetail(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);
        return toRes(v);
    }

    // 방문차량 재사용 등록 (API-033)
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

    // 방문차량 수정 (API-034)
    public void updateVisitorVehicle(Long userId, Long visitorVehicleId, VisitorVehicleReq req) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);

        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }

        v.update(req.getLicensePlate(), req.getVisitorName(),
                req.getVisitPurpose(), req.getVisitDate());
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // 방문차량 삭제 (API-035)
    public void deleteVisitorVehicle(Long userId, Long visitorVehicleId) {
        findMyVehicle(userId, visitorVehicleId);
        visitorVehicleMapper.deleteVisitorVehicle(visitorVehicleId);
    }

    // 방문차량 등록 취소 (API-036)
    public void cancelVisitorVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = findMyVehicle(userId, visitorVehicleId);

        if (!"APPROVED".equals(v.getStatus())) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_APPROVED);
        }

        v.cancel();
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // 관리자 방문 예정 현황 조회 (API-069)
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
}