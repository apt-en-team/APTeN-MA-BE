package com.apt.user.mapper;

import com.apt.user.dto.response.AdminUserRes;
import com.apt.user.dto.response.UserSearchRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 관리자 회원 관리 매퍼
// - 회원 승인/거부
// - 입주민 검색 (동/호수 기준)
@Mapper
public interface AdminUserMapper {

    // 유저 단건 조회 (승인/거부 전 존재 확인용)
    AdminUserRes findById(@Param("userId") Long userId);

    // 유저 상태 변경 (APPROVED / REJECTED)
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);

    // 세대 입주 이력 등록
    // household_history: household_id, user_id, status='입주', changed_at=NOW()
    void insertHistory(@Param("householdId") Long householdId, @Param("userId") Long userId);

    // 입주민 검색 (동/호수 기준)
    // dong: 선택 (null이면 전체 동 검색)
    // ho: 필수
    // user JOIN household → dong/ho 조건으로 APPROVED 입주민 리스트 반환
    List<UserSearchRes> searchByHo(@Param("dong") String dong, @Param("ho") String ho);

    // 승인 상태별 사용자 목록 조회 (관리자 승인 대기 목록용)
    List<UserSearchRes> findByStatus(@Param("status") String status);
}