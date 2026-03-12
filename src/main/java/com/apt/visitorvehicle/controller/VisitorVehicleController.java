package com.apt.visitorvehicle.controller;

import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.VisitorVehicleRes;
import com.apt.visitorvehicle.service.VisitorVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}