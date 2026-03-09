package com.apt.auth.controller;

import com.apt.auth.dto.request.LinkHouseholdReq;
import com.apt.auth.dto.request.UserSignInReq;
import com.apt.auth.dto.request.UserSignUpReq;
import com.apt.auth.dto.response.UserSignInRes;
import com.apt.auth.service.AuthService;
import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 인증 관련 API 컨트롤러 (회원가입, 로그인, 로그아웃, 토큰 재발급, 소셜 연동)
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 (POST /api/auth/register)
    // 인증 불필요 - SecurityConfig에서 permitAll 설정
    @PostMapping("/register")
    public ResultResponse<Void> register(@Valid @RequestBody UserSignUpReq req) {
        authService.signUp(req);
        return new ResultResponse<>("회원가입 성공", null);
    }

    // 로그인 (POST /api/auth/login)
    // 성공 시 AT/RT를 HttpOnly 쿠키로 발급, body에는 사용자 정보만 반환
    @PostMapping("/login")
    public ResultResponse<UserSignInRes> login(@Valid @RequestBody UserSignInReq req,
                                               HttpServletResponse res) {
        UserSignInRes result = authService.signIn(req, res);
        return new ResultResponse<>("로그인 성공", result);
    }

    // 로그아웃 (POST /api/auth/logout)
    // DB에서 RT 삭제 + AT/RT 쿠키 만료 처리
    @PostMapping("/logout")
    public ResultResponse<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       HttpServletResponse res) {
        authService.signOut(userPrincipal.getUserId(), res);
        return new ResultResponse<>("로그아웃 성공", null);
    }

    // AT 재발급 (POST /api/auth/refresh)
    // 쿠키의 RT를 검증해 새 AT 발급
    @PostMapping("/refresh")
    public ResultResponse<Void> refresh(HttpServletRequest req, HttpServletResponse res) {
        authService.refreshAccessToken(req, res);
        return new ResultResponse<>("토큰 재발급 성공", null);
    }

    // 소셜 로그인 후 동호수 연결 (PATCH /api/auth/link-household)
    // 소셜 로그인 신규 유저가 동호수 입력 시 household 연결 + APPROVED 처리
    @PatchMapping("/link-household")
    public ResultResponse<Void> linkHousehold(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @Valid @RequestBody LinkHouseholdReq req) {
        authService.linkHousehold(userPrincipal.getUserId(), req);
        return new ResultResponse<>("동호수 연결 성공", null);
    }

    // 이메일 중복 확인 (GET /api/auth/check-email?email=xxx)
    // 회원가입 전 이메일 사용 가능 여부 확인, 인증 불필요
    @GetMapping("/check-email")
    public ResultResponse<Void> checkEmail(@RequestParam String email) {
        authService.checkEmail(email);
        return new ResultResponse<>("사용 가능한 이메일입니다", null);
    }
}