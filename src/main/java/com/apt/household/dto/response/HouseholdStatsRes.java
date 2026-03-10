package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;

// 프론트엔드 세대 관리 페이지 상단 통계 카드용 응답 DTO
// GET /api/admin/households/stats
@Getter
@Setter
public class HouseholdStatsRes {

    // 전체 세대 수 (household 테이블 전체 COUNT)
    private Long total;;

    // 입주 중인 세대 수 (household_history 최신 status = '입주')
    private Long occupied;

    // 공실 세대 수 (이력 없거나 최신 status = '퇴거')
    private Long empty;

    // 이번 달 전입 건수 (household_history status='입주' AND 이번달)
    private Long moveIn;

    // 이번 달 전출 건수 (household_history status='퇴거' AND 이번달)
    private Long moveOut;

    // 30일 이상 공실 건수
    private Long needCare;
}





























