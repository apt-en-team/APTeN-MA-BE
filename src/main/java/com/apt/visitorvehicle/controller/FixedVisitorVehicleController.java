package com.apt.visitorvehicle.controller;

import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.FixedVisitorVehicleReq;
import com.apt.visitorvehicle.dto.response.FixedVisitorVehicleRes;
import com.apt.visitorvehicle.service.FixedVisitorVehicleService;
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
public class FixedVisitorVehicleController {
    private final FixedVisitorVehicleService fixedVisitorVehicleService;

    // API-062 | 고정 방문차량 등록
    @PostMapping("/fixed")
    public ResponseEntity<FixedVisitorVehicleRes> fixedRegisterVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody FixedVisitorVehicleReq req) {
        FixedVisitorVehicleRes res = fixedVisitorVehicleService.registerVisitorVehicle(
                userPrincipal.getUserId(), req);
        return ResponseEntity.ok(res);
    }
}
