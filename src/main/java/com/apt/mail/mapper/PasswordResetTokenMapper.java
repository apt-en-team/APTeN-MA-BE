package com.apt.mail.mapper;

import com.apt.mail.model.PasswordResetToken;
import org.apache.ibatis.annotations.Mapper;

// 비밀번호 재설정 토큰 DB 접근
@Mapper
public interface PasswordResetTokenMapper {

    // 토큰 저장
    void saveToken(PasswordResetToken token);

    // 토큰으로 조회
    PasswordResetToken findByToken(String token);

    // 사용자 ID로 기존 토큰 삭제 (재발급 시 기존 토큰 제거)
    void deleteByUserId (Long userId);
}
