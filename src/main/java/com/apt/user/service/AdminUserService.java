package com.apt.user.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.user.dto.response.AdminUserRes;
import com.apt.user.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 관리자 회원 승인/거부 서비스
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    // 회원 승인
    // 1. 유저 조회 (없으면 404)
    // 2. user.status → APPROVED
    // 3. household_id 있으면 household_history '입주' 이력 등록
    @Transactional
    public void approveUser(Long userId) {
        // 유저 조회
        AdminUserRes user = adminUserMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // status → APPROVED
        adminUserMapper.updateStatus(userId, "APPROVED");

        // household_id 있으면 입주 이력 등록
        if (user.getHouseholdId() != null) {
            adminUserMapper.insertHistory(user.getHouseholdId(), userId);
        }
    }

    // 회원 거부
    // user.status → REJECTED
    @Transactional
    public void rejectUser(Long userId) {
        AdminUserRes user = adminUserMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        adminUserMapper.updateStatus(userId, "REJECTED");
    }
}
