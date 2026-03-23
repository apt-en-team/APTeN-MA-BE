package com.apt.config.oauth2;

import com.apt.auth.dto.request.AuthToken;
import com.apt.auth.mapper.AuthMapper;
import com.apt.common.security.JwtUser;
import com.apt.common.security.UserPrincipal;
import com.apt.config.security.JwtTokenManager;
import com.apt.config.security.JwtTokenProvider;
import com.apt.user.mapper.UserMapper;
import com.apt.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

// 소셜 로그인 성공 시 JWT 발급 + 승인 상태(status)에 따라 Vue 앱으로 분기 리다이렉트
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // AT/RT 발급 및 쿠키 저장 총괄
    private final JwtTokenManager jwtTokenManager;

    // RT 생성 (DB 저장용)
    private final JwtTokenProvider jwtTokenProvider;

    // refresh_token 테이블 접근 (RT 저장/삭제)
    private final AuthMapper authMapper;

    // user 테이블 접근 (승인 상태 조회)
    private final UserMapper userMapper;

    // 소셜 로그인 성공 후 리다이렉트할 Vue 앱 기본 주소
    private static final String REDIRECT_URL = "http://localhost:5173/oauth2/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 인증 완료된 사용자 정보 추출
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        JwtUser jwtUser = new JwtUser(userPrincipal.getUserId(), userPrincipal.getRole());

        // AT/RT를 HttpOnly 쿠키로 발급
        jwtTokenManager.issue(response, jwtUser);

        // 기존 RT 삭제 후 새 RT DB 저장 (재로그인 시 중복 방지)
        String refreshToken = jwtTokenProvider.generateRefreshToken(jwtUser);
        authMapper.deleteRefreshTokenByUserId(jwtUser.getUserId());

        AuthToken authToken = new AuthToken();
        authToken.setUserId(jwtUser.getUserId());
        authToken.setRefreshToken(refreshToken);
        authToken.setExpiredAt(LocalDateTime.now().plusDays(7));
        authMapper.saveRefreshToken(authToken);

        // DB에서 현재 사용자의 승인 상태 조회
        User user = userMapper.findUserById(jwtUser.getUserId());
        String status = user.getStatus();

        // 승인 상태에 따라 리다이렉트 URL 분기
        // APPROVED  → 전체 메뉴 접근 가능 (정상 로그인)
        // PENDING   → 대시보드/마이페이지만 접근 가능 (관리자 승인 대기 중)
        // REJECTED  → 로그인 불가 (거절된 계정)
        String redirectUrl;
        if ("APPROVED".equals(status)) {
            redirectUrl = REDIRECT_URL + "?role=" + jwtUser.getRole();
        } else if ("PENDING".equals(status)) {
            redirectUrl = REDIRECT_URL + "?role=" + jwtUser.getRole() + "&status=PENDING";
        } else {
            redirectUrl = "http://localhost:5173/login?error=rejected";
        }

        log.info("소셜 로그인 성공 - userId: {}, role: {}, status: {}",
                jwtUser.getUserId(), jwtUser.getRole(), status);

        // Vue 앱으로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}