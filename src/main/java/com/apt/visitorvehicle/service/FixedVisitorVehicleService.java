package com.apt.visitorvehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.visitorvehicle.dto.request.FixedVisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleRes;
import com.apt.visitorvehicle.mapper.FixedVisitorVehicleMapper;
import com.apt.visitorvehicle.model.FixedVisitorVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FixedVisitorVehicleService {
    private final FixedVisitorVehicleMapper fixedVisitorVehicleMapper;

    // API-062 | 고정 방문차량 등록
    public FixedVisitorVehicleRes registerVisitorVehicle(Long userId, FixedVisitorVehicleReq req) {
        // 1. 시작일 과거 날짜 검증
        if (req.getStartDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        // 2. 종료일이 시작일보다 앞인지 검증
        if (req.getEndDate() != null && req.getEndDate().isBefore(req.getStartDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }
        // 3. Model 객체 세팅
        FixedVisitorVehicle fixedVisitorVehicle = new FixedVisitorVehicle();
        fixedVisitorVehicle.setUserId(userId);
        fixedVisitorVehicle.setVehicleNumber(req.getVehicleNumber());
        fixedVisitorVehicle.setVisitorName(req.getVisitorName());
        fixedVisitorVehicle.setPurpose(req.getPurpose());
        fixedVisitorVehicle.setStartDate(req.getStartDate());
        fixedVisitorVehicle.setEndDate(req.getEndDate());

        // 4. DB INSERT (useGeneratedKeys로 fixedId 자동 세팅)
        fixedVisitorVehicleMapper.insertFixedVisitorVehicle(fixedVisitorVehicle);

        // 5. 응답 DTO 변환
        FixedVisitorVehicleRes res = new FixedVisitorVehicleRes();
        res.setFixedId(fixedVisitorVehicle.getFixedId());
        res.setVehicleNumber(fixedVisitorVehicle.getVehicleNumber());
        res.setVisitorName(fixedVisitorVehicle.getVisitorName());
        res.setPurpose(fixedVisitorVehicle.getPurpose());
        res.setStartDate(fixedVisitorVehicle.getStartDate());
        res.setEndDate(fixedVisitorVehicle.getEndDate());
        res.setCreatedAt(LocalDateTime.now());
        return res;
    }
}
