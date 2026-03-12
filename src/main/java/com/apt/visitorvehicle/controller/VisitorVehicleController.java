package com.apt.visitorvehicle.controller;

import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
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

    // API-030 | 방문차량 사전등록 (등록 즉시 자동 승인)
    @PostMapping
    public ResponseEntity<VisitorVehicleRes> registerVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,  // JWT에서 인증된 사용자 정보
            @Valid @RequestBody VisitorVehicleReq req) {
        VisitorVehicleRes res = visitorVehicleService.registerVisitorVehicle(
                userPrincipal.getUserId(), req);  // UserPrincipal에서 userId 추출
        return ResponseEntity.ok(res);
    }

    // API-031 | 내 방문차량 목록 조회 (필터 + 페이징)
    // @ModelAttribute: 쿼리스트링(?page=1&size=10&licensePlate=12가)을 VisitorVehicleGetReq 객체로 자동 바인딩
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyVisitorVehicles(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute VisitorVehicleGetReq req) {
        Map<String, Object> result = visitorVehicleService.getMyVisitorVehicles(
                userPrincipal.getUserId(), req);
        return ResponseEntity.ok(result);
    }
}