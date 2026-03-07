package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;

// 프론트엔드 세대 관리 페이지 상단 통계 카드용 응답 DTO
// GET /api/admin/households/stats
@Getter
@Setter
public class HouseholdStatsRes {

    // 전체 세대 수 (household 테이블 전체 COUNT)
    private Integer total;;

    // 입주 중인 세대 수 (household_history 최신 status = '입주')
    private Integer occupied;

    // 공실 세대 수 (이력 없거나 최신 status = '퇴거')
    private Integer empty;

    // 이번 달 전입 건수 (household_history status='입주' AND 이번달)
    private Integer moveIn;

    // 이번 달 전출 건수 (household_history status='퇴거' AND 이번달)
    private Integer moveOut;

    // 이번 달 전체 이력 건수 (moveIn + moveOut 합계)
    private Integer monthNew;
}





























