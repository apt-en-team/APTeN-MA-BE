package com.apt.user.controller;

import com.apt.user.service.AdminUserService;
import com.apt.user.dto.response.UserSearchRes;
import com.apt.common.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 관리자 회원 관리 API 컨트롤러
// 기본 URL: /api/admin
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // ── 회원 승인 
    // PATCH /api/admin/users/{userId}/approve
    // user.status → APPROVED
    // household_id 있으면 household_history '입주' 이력 자동 등록
    @PatchMapping("/users/{userId}/approve")
    public ResultResponse<?> approveUser(@PathVariable Long userId) {
        adminUserService.approveUser(userId);
        return ResultResponse.success("승인 완료", null);
    }

    // ── 회원 거부 
    // PATCH /api/admin/users/{userId}/reject
    // user.status → REJECTED
    @PatchMapping("/users/{userId}/reject")
    public ResultResponse<?> rejectUser(@PathVariable Long userId) {
        adminUserService.rejectUser(userId);
        return ResultResponse.success("거부 완료", null);
    }

    // ── 입주민 검색 ───────────────────────────────────────────────
    // GET /api/admin/users/search?dong=101동&ho=101호
    // household 테이블에서 dong/ho로 세대 조회 후 해당 입주민 리스트 반환
    @GetMapping("/users/search")
    public ResultResponse<List<UserSearchRes>> searchUser(
            @RequestParam(required = false) String dong,
            @RequestParam(required = false) String ho) {  // required = false 로 변경
        return ResultResponse.success("입주민 검색 성공", adminUserService.searchUser(dong, ho));
    }
}