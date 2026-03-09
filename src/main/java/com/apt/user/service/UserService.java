package com.apt.user.service;

import com.apt.auth.mapper.AuthMapper;
import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.config.security.JwtTokenManager;
import com.apt.user.dto.request.UpdateUserReq;
import com.apt.user.dto.response.UserGetMeRes;
import com.apt.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 사용자 정보 조회, 탈퇴 비즈니스 로직
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    // user 테이블 접근
    private final UserMapper userMapper;

    // refresh_token 테이블 접근 (탈퇴 시 RT 삭제용)
    private final AuthMapper authMapper;

    // 쿠키 만료 처리 (탈퇴 시 자동 로그아웃용)
    private final JwtTokenManager jwtTokenManager;

    // 마이페이지 내 정보 조회
    public UserGetMeRes getMe(Long userId) {
        UserGetMeRes result = userMapper.findById(userId);
        if (result == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return result;
    }

    // 회원 탈퇴 (소프트 딜리트)
    // 1. is_deleted=1, deleted_at=NOW() 업데이트
    // 2. RT DB 삭제 → 3. 쿠키 만료 → 자동 로그아웃
    @Transactional
    public void deactivate(Long userId, HttpServletResponse res) {
        userMapper.softDeleteUser(userId);
        authMapper.deleteRefreshTokenByUserId(userId);
        jwtTokenManager.expireCookies(res);
        log.info("회원 탈퇴 완료 - userId: {}", userId);
    }

    // 내 정보 수정 (이름, 전화번호)
    @Transactional
    public void updateMe(Long userId, UpdateUserReq req) {
        int result = userMapper.updateUser(userId, req);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        log.info("내 정보 수정 완료 - userId: {}", userId);
    }
}