package com.apt.visitorvehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
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

    // API-030 | 방문차량 사전등록
    public VisitorVehicleRes registerVisitorVehicle(Long userId, VisitorVehicleReq req) {
        // 1. 검증 - 과거 날짜면 에러 던짐
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
            // → 여기서 멈추고 프론트에 400 에러 응답이 감
        }

        // 2. 데이터 준비 - 프론트에서 받은 값을 DB에 넣을 객체로 세팅
        VisitorVehicle visitorVehicle = new VisitorVehicle();
        visitorVehicle.setUserId(userId); // 누가 등록했는지
        visitorVehicle.setLicensePlate(req.getLicensePlate()); // 차량번호
        visitorVehicle.setStatus("APPROVED"); // 자동 승인

        // 3. DB에 저장 - Mapper한테 넘김
        visitorVehicleMapper.insertVisitorVehicle(visitorVehicle);

        // 4. 응답 만들기 - DB에 저장된 결과를 프론트에 돌려줄 형태로 변환
        return toRes(visitorVehicle);
    }

    // API-031 | 내 방문차량 목록 조회
    public Map<String, Object> getMyVisitorVehicles(Long userId, VisitorVehicleGetReq req) {
        // 1. DB에서 필터 + 페이징 조건에 맞는 목록 조회 (최대 size개, 예: 10개)
        List<VisitorVehicle> list = visitorVehicleMapper.findByUserIdWithFilter(userId, req);

        // 2. 같은 필터 조건으로 전체 건수 조회 (페이징 계산용, LIMIT 없이 COUNT)
        int totalCount = visitorVehicleMapper.countByUserIdWithFilter(userId, req);

        // 3. Model → Response DTO 변환 (DB 객체를 프론트에 보낼 형태로 바꿈)
        List<VisitorVehicleRes> resList = list.stream().map(this::toRes).toList();

        // 4. 프론트에 보낼 응답 Map 구성 (JSON 객체로 변환됨)
        Map<String, Object> result = new HashMap<>();
        result.put("content", resList); // 목록 데이터 (최대 10개)
        result.put("page", req.getPage()); // 현재 페이지 번호 (1, 2, 3...)
        // 전체 페이지 수 계산: 19건 / 10개씩 = 1.9 → 올림 → 2페이지
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));
        result.put("totalCount", totalCount); // 필터 적용된 전체 건수 (페이징 "총 19건 중 10건 조회"에 사용)

        // 5. 사이드 카드용 통계 (필터 무관, 해당 유저 전체 기준)
        //    검색해도 이 숫자는 안 바뀜 (별도 쿼리로 조회)
        result.put("todayCount", visitorVehicleMapper.countTodayByUserId(userId)); // 오늘 방문 차량 수
        result.put("upcomingCount", visitorVehicleMapper.countUpcomingByUserId(userId)); // 예비 방문 차량 수
        result.put("allCount", visitorVehicleMapper.countAllByUserId(userId));  // 전체누적 등록 건수
        return result; // Controller → 프론트로 JSON 응답
    }

    // API-032 | 방문차량 상세 조회
    public VisitorVehicleRes getVisitorVehicleDetail(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
        if (!v.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return toRes(v);
    }

    // API-033 | 방문차량 재사용 등록
    public VisitorVehicleRes reuseVisitorVehicle(Long userId, Long visitorVehicleId, LocalDate visitDate) {
        VisitorVehicle original = visitorVehicleMapper.findById(visitorVehicleId);
        if (original == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
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

    // API-034 | 방문차량 수정
    public void updateVisitorVehicle(Long userId, Long visitorVehicleId, VisitorVehicleReq req) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
        if (!v.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        v.update(req.getLicensePlate(), req.getVisitorName(),
                req.getVisitPurpose(), req.getVisitDate());
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // API-035 | 방문차량 삭제
    public void deleteVisitorVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
        if (!v.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        visitorVehicleMapper.deleteVisitorVehicle(visitorVehicleId);
    }

    // API-036 | 등록 취소
    public void cancelVisitorVehicle(Long userId, Long visitorVehicleId) {
        VisitorVehicle v = visitorVehicleMapper.findById(visitorVehicleId);
        if (v == null) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        }
        if (!v.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (!"APPROVED".equals(v.getStatus())) {
            throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_APPROVED);
        }
        v.cancel();
        visitorVehicleMapper.updateVisitorVehicle(v);
    }

    // ── Model → Response 변환 공통 메서드 ──
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
}