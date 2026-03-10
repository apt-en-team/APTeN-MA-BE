package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

// API-014 세대 목록 조회 / API-015 세대 등록 / API-016 세대 수정 응답 DTO
// MyBatis SELECT 결과를 담는 응답 객체
@Getter
@Setter
public class HouseholdRes {

    // 세대 고유 ID
    private Long householdId;

    // 동 번호 (예: 101동)
    private String dong;

    // 호수 (예: 502호)
    private String ho;

    // 현재 입주 상태: '입주' / '퇴거' / '공실'
    // household_history 테이블에서 가장 최근 이력의 status 값
    // 이력이 없으면 '공실'로 표시 (MyBatis XML에서 COALESCE 처리)
    private String status;

    // 해당 세대에 등록된 차량 수 (vehicle 테이블 COUNT)
    private int carCount;

    //승인 요층 세대 수
    private int pendingCount;

    // 세대 등록일 or 마지막 이력일 중 최신
    private String lastChangedAt;

}