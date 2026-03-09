package com.apt.user.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.user.dto.request.UpdateUserReq;
import com.apt.user.dto.response.UserGetMeRes;
import com.apt.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 사용자 관련 API 컨트롤러 (내 정보 조회, 회원 탈퇴)
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회 (GET /api/users/me)
    // 마이페이지에서 사용자 정보 확인용
    @GetMapping("/me")
    public ResultResponse<UserGetMeRes> getMe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserGetMeRes result = userService.getMe(userPrincipal.getUserId());
        return new ResultResponse<>("내 정보 조회 성공", result);
    }

    // 회원 탈퇴 (PATCH /api/users/deactivate)
    // 본인이 직접 탈퇴 - is_deleted=1, deleted_at=NOW() 소프트 딜리트
    // 탈퇴 후 쿠키 만료 → 자동 로그아웃
    @PatchMapping("/deactivate")
    public ResultResponse<Void> deactivate(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           HttpServletResponse res) {
        userService.deactivate(userPrincipal.getUserId(), res);
        return new ResultResponse<>("탈퇴 처리 성공", null);
    }

    // 내 정보 수정 (PUT /api/users/me)
    @PutMapping("/me")
    public ResultResponse<Void> updateMe(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @Valid @RequestBody UpdateUserReq req) {
        userService.updateMe(userPrincipal.getUserId(), req);
        return new ResultResponse<>("내 정보 수정 성공", null);
    }
}