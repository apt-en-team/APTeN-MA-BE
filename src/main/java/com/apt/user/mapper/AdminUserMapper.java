package com.apt.user.mapper;

import com.apt.user.dto.response.AdminUserRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// 관리자 회원 승인/거부 매퍼
@Mapper
public interface AdminUserMapper {

    // 유저 단건 조회 (승인/거부 전 존재 확인용)
    AdminUserRes findById(@Param("userId") Long userId);

    // 유저 상태 변경 (APPROVED / REJECTED)
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);

    // 세대 입주 이력 등록
    // household_history: household_id, user_id, status='입주', changed_at=NOW()
    void insertHistory(@Param("householdId") Long householdId, @Param("userId") Long userId);
}
