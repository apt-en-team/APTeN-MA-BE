package com.apt.auth.controller;

import com.apt.auth.dto.*;
import com.apt.common.response.ResultResponse;
import com.apt.common.UserPrincipal;
import com.apt.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 인증 관련 API 컨트롤러 (회원가입, 로그인, 로그아웃, 토큰 재발급)
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    // 인증 비즈니스 로직
    private final UserService userService;

    // 회원가입 (POST /api/auth/register)
    // 인증 불필요 - SecurityConfig에서 permitAll 설정
    @PostMapping("/register")
    public ResultResponse<Void> register(@Valid @RequestBody UserSignUpReq req) {
        userService.signUp(req);
        return new ResultResponse<>("회원가입 성공", null);
    }

    // 로그인 (POST /api/auth/login)
    // 성공 시 AT/RT를 HttpOnly 쿠키로 발급, body에는 사용자 정보만 반환
    @PostMapping("/login")
    public ResultResponse<UserSignInRes> login(@Valid @RequestBody UserSignInReq req,
                                               HttpServletResponse res) {
        UserSignInRes result = userService.signIn(req, res);
        return new ResultResponse<>("로그인 성공", result);
    }

    // 로그아웃 (POST /api/auth/logout)
    // DB에서 RT 삭제 + AT/RT 쿠키 만료 처리
    @PostMapping("/logout")
    public ResultResponse<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       HttpServletResponse res) {
        userService.signOut(userPrincipal.getUserId(), res);
        return new ResultResponse<>("로그아웃 성공", null);
    }

    // AT 재발급 (POST /api/auth/refresh)
    // 쿠키의 RT를 검증해 새 AT 발급
    @PostMapping("/refresh")
    public ResultResponse<Void> refresh(HttpServletRequest req, HttpServletResponse res) {
        userService.refreshAccessToken(req, res);
        return new ResultResponse<>("토큰 재발급 성공", null);
    }

    // 내 정보 조회 (GET /api/auth/me)
    // 마이페이지에서 사용자 정보 확인용
    @GetMapping("/me")
    public ResultResponse<UserGetMeRes> getMe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserGetMeRes result = userService.getMe(userPrincipal.getUserId());
        return new ResultResponse<>("내 정보 조회 성공", result);
    }

    // 회원 탈퇴 (PATCH /api/auth/deactivate)
    // 본인이 직접 탈퇴 - is_deleted=1, deleted_at=NOW() 소프트 딜리트
    // 탈퇴 후 쿠키 만료 → 자동 로그아웃
    @PatchMapping("/deactivate")
    public ResultResponse<Void> deactivate(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           HttpServletResponse res) {
        userService.deactivate(userPrincipal.getUserId(), res);
        return new ResultResponse<>("탈퇴 처리 성공", null);
    }

    // 소셜 로그인 후 동호수 연결 (PATCH /api/auth/link-household)
    // 소셜 로그인 신규 유저가 동호수 입력 시 household 연결 + APPROVED 처리
    @PatchMapping("/link-household")
    public ResultResponse<Void> linkHousehold(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @Valid @RequestBody LinkHouseholdReq req) {
        userService.linkHousehold(userPrincipal.getUserId(), req);
        return new ResultResponse<>("동호수 연결 성공", null);
    }

    // 이메일 중복 확인 (GET /api/auth/check-email?email=xxx)
    // 회원가입 전 이메일 사용 가능 여부 확인
    // 인증 불필요 - SecurityConfig에서 permitAll 설정
    @GetMapping("/check-email")
    public ResultResponse<Void> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return new ResultResponse<>("사용 가능한 이메일입니다", null);
    }
}
