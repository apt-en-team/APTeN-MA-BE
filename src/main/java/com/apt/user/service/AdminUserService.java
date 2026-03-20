package com.apt.user.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.user.dto.response.AdminUserRes;
import com.apt.user.dto.response.UserSearchRes;
import com.apt.user.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// 관리자 회원 관리 서비스
// - 회원 승인/거부
// - 입주민 검색 (동/호수 기준)
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    // ── 회원 승인 
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

    // ── 회원 거부 
    // user.status → REJECTED
    @Transactional
    public void rejectUser(Long userId) {
        // 유저 조회 (없으면 404)
        AdminUserRes user = adminUserMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        // status → REJECTED
        adminUserMapper.updateStatus(userId, "REJECTED");
    }

    // ── 입주민 검색 ───────────────────────────────────────────────
    // dong: 선택 (null이면 전체 동 검색)
    // household 테이블에서 dong/ho로 세대 조회 후 해당 입주민 리스트 반환
    public List<UserSearchRes> searchUser(String dong, String ho) {
        // dong, ho 둘 다 없으면 예외
        if ((dong == null || dong.isBlank()) && (ho == null || ho.isBlank())) {
            throw new IllegalArgumentException("동 또는 호수를 입력해주세요.");
        }
        return adminUserMapper.searchByHo(dong, ho);
    }

    // 승인 대기 중인 사용자 목록 조회
    // status = PENDING 인 유저를 조회 후 UserSearchRes DTO로 변환해서 반환
    public List<UserSearchRes> getPendingUsers() {
        return adminUserMapper.findByStatus("PENDING"); // ← adminUserMapper 사용
    }
}