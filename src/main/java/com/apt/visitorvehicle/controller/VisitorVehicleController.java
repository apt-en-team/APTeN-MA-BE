package com.apt.visitorvehicle.controller;

import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReuseReq;
import com.apt.visitorvehicle.dto.response.VisitorVehicleRes;
import com.apt.visitorvehicle.service.VisitorVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/visitor-vehicles")
@RequiredArgsConstructor
public class VisitorVehicleController {
    private final VisitorVehicleService visitorVehicleService;

    // API-030 | 방문차량 사전등록
    @PostMapping  // POST /api/visitor-vehicles 요청이 오면 이 메서드가 실행됨
    public ResponseEntity<VisitorVehicleRes> registerVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal, // JWT 쿠키에서 로그인한 유저 정보 자동 추출
            @Valid @RequestBody VisitorVehicleReq req) {  // Body의 JSON을 VisitorVehicleReq 객체로 자동 변환
        VisitorVehicleRes res = visitorVehicleService.registerVisitorVehicle(
                userPrincipal.getUserId(), req); // Service로 넘김
        return ResponseEntity.ok(res); // 200 OK + 결과 JSON 응답
    }

    // API-031 | 내 방문차량 목록 조회
    // GET /api/visitor-vehicles/my?page=1&size=10&licensePlate=12가 요청이 오면 실행
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyVisitorVehicles(
            @AuthenticationPrincipal UserPrincipal userPrincipal,  // JWT 쿠키에서 로그인 유저 추출
            @ModelAttribute VisitorVehicleGetReq req) { // URL 쿼리스트링을 자바 객체로 변환
        Map<String, Object> result = visitorVehicleService.getMyVisitorVehicles(
                userPrincipal.getUserId(), req);  // Service에 userId와 필터+페이징 조건 넘김
        return ResponseEntity.ok(result);  // 200 OK + JSON 응답
    }

    // API-032 | 방문차량 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<VisitorVehicleRes> getVisitorVehicleDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        VisitorVehicleRes res = visitorVehicleService.getVisitorVehicleDetail(
                userPrincipal.getUserId(), visitorVehicleId);
        return ResponseEntity.ok(res);
    }

    // API-033 | 방문차량 재사용 등록
    @PostMapping("/{id}/reuse")
    public ResponseEntity<VisitorVehicleRes> reuseVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId,
            @Valid @RequestBody VisitorVehicleReuseReq req) {
        VisitorVehicleRes res = visitorVehicleService.reuseVisitorVehicle(
                userPrincipal.getUserId(), visitorVehicleId, req.getVisitDate());
        return ResponseEntity.ok(res);
    }

    // API-034 | 방문차량 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId,
            @Valid @RequestBody VisitorVehicleReq req) {
        visitorVehicleService.updateVisitorVehicle(
                userPrincipal.getUserId(), visitorVehicleId, req);
        return ResponseEntity.ok().build();
    }

    // API-035 | 방문차량 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        visitorVehicleService.deleteVisitorVehicle(
                userPrincipal.getUserId(), visitorVehicleId);
        return ResponseEntity.noContent().build();
    }

    // API-036 | 등록 취소
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        visitorVehicleService.cancelVisitorVehicle(
                userPrincipal.getUserId(), visitorVehicleId);
        return ResponseEntity.ok().build();
    }

}