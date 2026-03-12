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

    /**
     * 방문차량 사전등록 (API-030)
     * 입주민이 방문 예정 차량을 등록하면 즉시 자동 승인(APPROVED) 처리
     *
     * @param userId 현재 로그인한 입주민 ID (Controller에서 JWT 토큰으로부터 추출)
     * @param req    프론트에서 보낸 등록 요청 데이터 (차량번호, 방문자명, 방문목적, 방문예정일)
     * @return 등록 완료된 방문차량 정보
     * @throws CustomException PAST_VISIT_DATE(400) - 과거 날짜 선택 시
     */
    public VisitorVehicleRes registerVisitorVehicle(Long userId, VisitorVehicleReq req) {
        // 1. 과거 날짜 체크: 방문 예정일이 오늘보다 이전이면 400 에러
        if (req.getVisitDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.PAST_VISIT_DATE);
        }
        // 2. Model 세팅: 프론트에서 받은 요청 데이터 + userId를 DB에 넣을 객체로 변환
        VisitorVehicle visitorVehicle = new VisitorVehicle();
        visitorVehicle.setUserId(userId);                          // 로그인한 입주민 ID
        visitorVehicle.setLicensePlate(req.getLicensePlate());     // 차량번호
        visitorVehicle.setVisitorName(req.getVisitorName());       // 방문자 이름
        visitorVehicle.setVisitPurpose(req.getVisitPurpose());     // 방문 목적
        visitorVehicle.setVisitDate(req.getVisitDate());           // 방문 예정일
        visitorVehicle.setStatus("APPROVED");                      // 자동 승인 처리
        // 3. DB INSERT: MyBatis가 INSERT 실행 후 생성된 PK(visitorVehicleId)를 객체에 자동 세팅
        visitorVehicleMapper.insertVisitorVehicle(visitorVehicle);
        // 4. Response 변환: DB에 저장된 결과를 프론트에 돌려줄 응답 객체로 변환
        VisitorVehicleRes res = new VisitorVehicleRes();
        res.setVisitorVehicleId(visitorVehicle.getVisitorVehicleId()); // INSERT 후 자동 생성된 PK
        res.setUserId(userId);
        res.setLicensePlate(visitorVehicle.getLicensePlate());
        res.setVisitorName(visitorVehicle.getVisitorName());
        res.setVisitDate(visitorVehicle.getVisitDate());
        res.setVisitPurpose(visitorVehicle.getVisitPurpose());
        res.setStatus(visitorVehicle.getStatus());
        res.setCreatedAt(LocalDateTime.now());  // 등록 시점 현재 시간
        return res;  // Controller → 프론트로 응답
    }

    /**
     * 내 방문차량 목록 조회 (API-031)
     * 로그인한 입주민이 등록한 방문차량 목록 반환 (필터 + 페이징, 최신순)
     *
     * @param userId 현재 로그인한 입주민 ID
     * @param req    필터 조건 (차량번호, 방문자명, 방문일) + 페이징 (page, size)
     * @return content: 방문차량 목록, page: 현재 페이지, totalPages: 전체 페이지 수, totalCount: 전체 건수
     */
    public Map<String, Object> getMyVisitorVehicles(Long userId, VisitorVehicleGetReq req) {
        // 1. 필터 + 페이징 조건으로 목록 조회
        List<VisitorVehicle> list = visitorVehicleMapper.findByUserIdWithFilter(userId, req);
        // 2. 동일 조건으로 전체 건수 조회 (페이징 계산용)
        int totalCount = visitorVehicleMapper.countByUserIdWithFilter(userId, req);
        // 3. Model → Response DTO 변환
        List<VisitorVehicleRes> resList = list.stream().map(v -> {
            VisitorVehicleRes res = new VisitorVehicleRes();
            res.setVisitorVehicleId(v.getVisitorVehicleId());
            res.setUserId(v.getUserId());
            res.setLicensePlate(v.getLicensePlate());
            res.setVisitorName(v.getVisitorName());
            res.setVisitDate(v.getVisitDate());
            res.setVisitPurpose(v.getVisitPurpose());
            res.setStatus(v.getStatus());
            res.setCreatedAt(v.getCreatedAt());
            return res;
        }).toList();
        // 4. 페이징 응답 구성: 프론트에서 content, page, totalPages, totalCount로 받아서 사용
        Map<String, Object> result = new HashMap<>();
        result.put("content", resList);                                                    // 목록 데이터
        result.put("page", req.getPage());                                                 // 현재 페이지
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));    // 전체 페이지 수
        result.put("totalCount", totalCount);                                              // 전체 건수
        return result;
    }
}