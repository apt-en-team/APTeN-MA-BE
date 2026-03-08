package com.apt.config.oauth2;

import com.apt.auth.dto.request.AuthToken;
import com.apt.auth.mapper.AuthMapper;
import com.apt.common.security.JwtUser;
import com.apt.common.security.UserPrincipal;
import com.apt.config.security.JwtTokenManager;
import com.apt.config.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

// 소셜 로그인 성공 시 JWT AT/RT 발급 후 Vue 앱으로 리다이렉트
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

    // 소셜 로그인 성공 후 리다이렉트할 Vue 앱 주소
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

        log.info("소셜 로그인 성공 - userId: {}, role: {}", jwtUser.getUserId(), jwtUser.getRole());

        // Vue 앱 소셜 로그인 콜백 페이지로 리다이렉트
        // 프론트에서 role 기반 대시보드로 다시 이동함
        response.sendRedirect(REDIRECT_URL + "?role=" + jwtUser.getRole());
    }
}