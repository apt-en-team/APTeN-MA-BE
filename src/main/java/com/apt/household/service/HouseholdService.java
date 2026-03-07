package com.apt.household.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.household.dto.request.HouseholdHistoryReq;
import com.apt.household.dto.request.HouseholdReq;
import com.apt.household.dto.response.HouseholdHistoryRes;
import com.apt.household.dto.response.HouseholdRes;
import com.apt.household.dto.response.HouseholdStatsRes;
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
    // page: 현재 페이지 번호 (0부터 시작)
    // size: 페이지당 데이터 개수
    // 반환: { content, page, size, totalElements, totalPages }
    public Map<String, Object> getHouseholds(int page, int size) {
        int offset         = page * size;
        List<HouseholdRes> content       = householdMapper.findAll(offset, size);
        int                totalElements = householdMapper.countAll();
        int                totalPages    = (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content",       content);
        result.put("page",          page);
        result.put("size",          size);
        result.put("totalElements", totalElements);
        result.put("totalPages",    totalPages);
        return result;
    }

    // ── 통계 조회 (프론트 상단 카드용) ───────────────────────────
    // 전체/입주/공실/이번달 전입·전출 건수 반환
    public HouseholdStatsRes getStats() {
        return householdMapper.getStats();
    }

    // ── API-015: 세대 등록 ────────────────────────────────────────
    // 동/호 중복 확인 후 household 테이블에 INSERT
    // 새로 등록된 세대는 이력이 없으므로 status = '공실', carCount = 0
    @Transactional
    public HouseholdRes createHousehold(HouseholdReq req) {

        // 동/호 중복 확인 (DB UNIQUE 제약 대비 사전 검증)
        if (householdMapper.existsByDongAndHo(req.getDong(), req.getHo()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_HOUSEHOLD);
        }

        // Household 객체 생성 후 INSERT
        Household household = new Household();
        household.setDong(req.getDong());
        household.setHo(req.getHo());
        householdMapper.save(household);  // INSERT 후 householdId 자동 주입

        // 응답 객체 생성
        HouseholdRes res = new HouseholdRes();
        res.setHouseholdId(household.getHouseholdId());
        res.setDong(household.getDong());
        res.setHo(household.getHo());
        res.setCreatedAt(household.getCreatedAt());
        res.setStatus("공실");   // 신규 세대는 이력 없으므로 공실
        res.setCarCount(0);      // 신규 세대는 차량 없음
        return res;
    }

    // ── API-016: 세대 수정 ────────────────────────────────────────
    // 존재 여부 확인 후 dong, ho 변경
    @Transactional
    public HouseholdRes updateHousehold(Long householdId, HouseholdReq req) {

        // 세대 존재 여부 확인
        Household existing = householdMapper.findById(householdId);
        if (existing == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        // 값 변경 후 UPDATE
        existing.setDong(req.getDong());
        existing.setHo(req.getHo());
        householdMapper.update(existing);

        // 응답 객체 생성
        HouseholdRes res = new HouseholdRes();
        res.setHouseholdId(existing.getHouseholdId());
        res.setDong(existing.getDong());
        res.setHo(existing.getHo());
        res.setCreatedAt(existing.getCreatedAt());
        return res;
    }

    // ── API-017: 세대 삭제 ────────────────────────────────────────
    // 소속 회원(is_deleted=0)이 있으면 삭제 불가 (FR-010)
    @Transactional
    public void deleteHousehold(Long householdId) {

        // 세대 존재 여부 확인
        if (householdMapper.findById(householdId) == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        // 소속 회원 있으면 삭제 불가
        if (householdMapper.countUsersByHouseholdId(householdId) > 0) {
            throw new CustomException(ErrorCode.HOUSEHOLD_HAS_MEMBER);
        }

        householdMapper.delete(householdId);
    }

    // ── API-018: 입주/퇴거 이력 등록 ─────────────────────────────
    // household_history 테이블에 이력 행 INSERT
    // 이력은 삭제하지 않고 누적 관리 (FR-011)
    @Transactional
    public HouseholdHistoryRes createHistory(Long householdId, HouseholdHistoryReq req) {

        // 세대 존재 여부 확인
        if (householdMapper.findById(householdId) == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        // HouseholdHistory 객체 생성 후 INSERT
        HouseholdHistory history = new HouseholdHistory();
        history.setHouseholdId(householdId);
        history.setUserId(req.getUserId());
        history.setStatus(req.getStatus());   // '입주' 또는 '퇴거'
        householdMapper.insertHistory(history);  // INSERT 후 historyId 자동 주입

        // 응답 객체 생성
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
}






























