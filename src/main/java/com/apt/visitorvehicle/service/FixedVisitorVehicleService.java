package com.apt.visitorvehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.visitorvehicle.dto.request.FixedVisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleListRes;
import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleRes;
import com.apt.visitorvehicle.mapper.FixedVisitorVehicleMapper;
import com.apt.visitorvehicle.model.FixedVisitorVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedVisitorVehicleService {
    private final FixedVisitorVehicleMapper fixedVisitorVehicleMapper;

    // API-062 | 고정 방문차량 등록
    public FixedVisitorVehicleRes registerVisitorVehicle(Long userId, FixedVisitorVehicleReq req) {
        if (req.getStartDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        if (req.getEndDate() != null && req.getEndDate().isBefore(req.getStartDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }
        FixedVisitorVehicle v = new FixedVisitorVehicle();
        v.setUserId(userId);
        v.setVehicleNumber(req.getVehicleNumber());
        v.setVisitorName(req.getVisitorName());
        v.setPurpose(req.getPurpose());
        v.setStartDate(req.getStartDate());
        v.setEndDate(req.getEndDate());

        fixedVisitorVehicleMapper.insertFixedVisitorVehicle(v);

        FixedVisitorVehicleRes res = new FixedVisitorVehicleRes();
        res.setFixedId(v.getFixedId());
        res.setVehicleNumber(v.getVehicleNumber());
        res.setVisitorName(v.getVisitorName());
        res.setPurpose(v.getPurpose());
        res.setStartDate(v.getStartDate());
        res.setEndDate(v.getEndDate());
        res.setCreatedAt(LocalDateTime.now());
        return res;
    }

    // API-063 | 내 고정 방문차량 목록 조회 (필터 + 페이징 + 사이드 통계)
    public FixedVisitorVehicleListRes getMyFixedVisitorVehicles(Long userId, String vehicleNumber, int page, int size) {
        int startIdx = (page - 1) * size;
        List<FixedVisitorVehicleRes> list = fixedVisitorVehicleMapper.findByMyFixed(userId, vehicleNumber, startIdx, size);
        int totalCount = fixedVisitorVehicleMapper.countByMyFixed(userId, vehicleNumber);

        FixedVisitorVehicleListRes result = new FixedVisitorVehicleListRes();
        result.setContent(list);
        result.setPage(page);
        result.setTotalPages((int) Math.ceil((double) totalCount / size));
        result.setTotalCount(totalCount);
        // 사이드 카드 통계 (필터 무관)
        result.setAllCount(fixedVisitorVehicleMapper.countAllByUserId(userId));
        result.setUnlimitedCount(fixedVisitorVehicleMapper.countUnlimitedByUserId(userId));
        return result;
    }

    // 조회 + 본인 확인 (없으면 404, 본인 아니면 403)
    private FixedVisitorVehicle findMyFixed(Long userId, Long fixedId) {
        FixedVisitorVehicle v = fixedVisitorVehicleMapper.findFixedById(fixedId);
        if (v == null) throw new CustomException(ErrorCode.VISITOR_VEHICLE_NOT_FOUND);
        if (!v.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);
        return v;
    }

    // API-065 | 고정 방문차량 소프트 삭제
    public void deleteFixedVisitorVehicle(Long userId, Long fixedId) {
        findMyFixed(userId, fixedId);
        fixedVisitorVehicleMapper.deleteFixedVisitorVehicle(fixedId);
    }
}