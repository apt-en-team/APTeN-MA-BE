package com.apt.household.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.household.dto.request.HouseholdGetReq;
import com.apt.household.dto.request.HouseholdHistoryReq;
import com.apt.household.dto.request.HouseholdReq;
import com.apt.household.dto.response.*;
import com.apt.household.mapper.HouseholdMapper;
import com.apt.household.model.Household;
import com.apt.household.model.HouseholdHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 세대 관련 비즈니스 로직 처리 클래스
// FR-010: 세대 등록/수정/삭제 (소속 회원 있으면 삭제 불가)
// FR-011: 세대 입주/퇴거 이력 등록 및 조회
@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdMapper householdMapper;

    // ── API-014: 세대 목록 조회 (페이징) ─────────────────────────
    public List<HouseholdRes> getHouseholds(HouseholdGetReq req) {
        return householdMapper.findAll(req);
    }
    public PageRes getMaxPage(HouseholdGetReq req) {
        return householdMapper.getPageInfo(req);
    }
    public List<String> getAllDongs() {
        return householdMapper.findAllDongs();
    }

    public List<PendingUserDto> getPendingUsers(Long householdId) {
        return householdMapper.findPendingUsers(householdId);
    }

    // ── 통계 조회 (프론트 상단 카드용) ───────────────────────────
    // 전체/입주/공실/이번달 전입·전출 건수 반환
    public HouseholdStatsRes getStats() {
        return householdMapper.getStats();
    }

    // ── API-018: 입주/퇴거 이력 등록 ─────────────────────────────
    // household_history 테이블에 이력 행 INSERT
    // 이력은 삭제하지 않고 누적 관리 (FR-011)
    @Transactional
    public HouseholdHistoryRes createHistory(Long householdId, HouseholdHistoryReq req) {

        if (householdMapper.findById(householdId) == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        HouseholdHistory history = new HouseholdHistory();
        history.setHouseholdId(householdId);
        history.setUserId(req.getUserId());
        history.setStatus(req.getStatus());
        householdMapper.insertHistory(history);

        // ← 퇴거 시 해당 유저 household_id → NULL
        if ("퇴거".equals(req.getStatus()) && req.getUserId() != null) {
            householdMapper.clearUserHousehold(req.getUserId());
        }

        HouseholdHistoryRes res = new HouseholdHistoryRes();
        res.setHistoryId(history.getHistoryId());
        res.setHouseholdId(history.getHouseholdId());
        res.setUserId(history.getUserId());
        res.setStatus(history.getStatus());
        res.setChangedAt(history.getChangedAt());
        return res;
    }

    // ── API-019: 입주/퇴거 이력 조회 ─────────────────────────────
    // 해당 세대의 전체 이력을 최신순으로 반환 (입주민 이름 포함)
    public List<HouseholdHistoryRes> getHistory(Long householdId) {

        // 세대 존재 여부 확인
        if (householdMapper.findById(householdId) == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        return householdMapper.findHistoryByHouseholdId(householdId);
    }
    // 모달 등록입주민 내역
    public List<ResidentDto> getResidents(Long householdId) {
        return householdMapper.findResidents(householdId);
    }

    public int getPendingResidentCount() {
        return householdMapper.countPendingResidents();
    }
}