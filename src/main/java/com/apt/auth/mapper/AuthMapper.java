package com.apt.auth.mapper;

import com.apt.auth.dto.request.AuthToken;
import org.apache.ibatis.annotations.Mapper;

// refresh_token 테이블 접근 MyBatis Mapper
@Mapper
public interface AuthMapper {

    // RT 저장 (로그인 시 DB에 RT 보관)
    int saveRefreshToken(AuthToken authToken);

    // userId로 RT 조회 (토큰 재발급 시 DB RT 검증)
    AuthToken findRefreshTokenByUserId(Long userId);

    // userId로 RT 삭제 (로그아웃, 재로그인 시 기존 RT 제거)
    int deleteRefreshTokenByUserId(Long userId);

    // RT 문자열로 RT 조회 (탈취 감지용)
    AuthToken findByRefreshToken(String refreshToken);
}