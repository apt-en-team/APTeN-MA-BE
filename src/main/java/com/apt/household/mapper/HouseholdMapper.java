package com.apt.household.mapper;

import com.apt.household.dto.request.HouseholdGetReq;
import com.apt.household.dto.request.HouseholdHistoryReq;
import com.apt.household.dto.response.*;
import com.apt.household.model.Household;
import com.apt.household.model.HouseholdHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// household, household_history 테이블 관련 MyBatis Mapper 인터페이스
// 실제 SQL 은 resources/mapper/HouseholdMapper.xml 에 작성
@Mapper
public interface HouseholdMapper {

    // ── 목록 / 조회 ──────────────────────────────────────────────

    // 세대 목록 조회 (페이징)
    // offset: 시작 위치, size: 가져올 개수
    List<HouseholdRes> findAll(HouseholdGetReq req);
    PageRes getPageInfo(HouseholdGetReq req);
    List<String> findAllDongs();

    // 세대 단건 조회 (존재 여부 확인 및 수정/삭제 전 검증에 사용)
    Household findById(@Param("householdId") Long householdId);

    // 세대 단건 조회 - 동/호 기준 (회원가입 시 사용자가 입력한 동/호로 세대 찾을 때 사용)
    Household findByDongAndHo(@Param("dong") String dong,
                              @Param("ho")   String ho);

    // 동/호 중복 확인 (세대 등록 전 중복 체크)
    // 반환값이 0이면 중복 없음, 1이면 이미 존재
    int existsByDongAndHo(@Param("dong") String dong,
                          @Param("ho")   String ho);

    // 소속 회원 수 조회 (삭제되지 않은 회원만 카운트)
    // 0이어야 세대 삭제 가능 (소속 회원 있으면 삭제 불가)
    int countUsersByHouseholdId(@Param("householdId") Long householdId);

    //승인요청
    List<PendingUserDto> findPendingUsers(@Param("householdId") Long householdId);

    // ── user ────

    // 세대 등록 (useGeneratedKeys=true → 생성된 PK가 household.householdId 에 자동 주입)
    void save(Household household);

    // ── 이력 ────

    // 입주/퇴거 이력 등록
    void insertHistory(HouseholdHistory history);

    //퇴거회원 householdId -> null 변경
    void clearUserHousehold(@Param("userId") Long userId);

    // 세대별 이력 목록 조회 (최신순 정렬, user.name JOIN)
    List<HouseholdHistoryRes> findHistoryByHouseholdId(
            @Param("householdId") Long householdId);
    // 모달 등록입주민 내역
    List<ResidentDto> findResidents(@Param("householdId") Long householdId);

    // ── 통계 ────

    // 프론트 상단 통계 카드용 집계 조회
    // (전체/입주/공실/이번달 전입·전출 건수)
    HouseholdStatsRes getStats();

    int countPendingResidents();
}