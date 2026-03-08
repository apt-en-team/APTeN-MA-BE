package com.apt.user.controller;

import com.apt.user.service.AdminUserService;
import com.apt.common.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 관리자 회원 승인/거부 API 컨트롤러
// 기본 URL: /api/admin
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // ── 회원 승인 ─────────────────────────────────────────────────
    // PATCH /api/admin/users/{userId}/approve
    // user.status → APPROVED
    // household_id 있으면 household_history '입주' 이력 자동 등록
    @PatchMapping("/users/{userId}/approve")
    public ResultResponse<?> approveUser(@PathVariable Long userId) {
        adminUserService.approveUser(userId);
        return ResultResponse.success("승인 완료", null);
    }

    // ── 회원 거부 ─────────────────────────────────────────────────
    // PATCH /api/admin/users/{userId}/reject
    // user.status → REJECTED
    @PatchMapping("/users/{userId}/reject")
    public ResultResponse<?> rejectUser(@PathVariable Long userId) {
        adminUserService.rejectUser(userId);
        return ResultResponse.success("거부 완료", null);
    }
}

