package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// API-018 이력 등록 / API-019 이력 조회 응답 DTO
// household_history 테이블 조회 결과를 담는 응답 객체
@Getter
@Setter
public class HouseholdHistoryRes {

    // 이력 고유 ID
    private Long historyId;

    // 이력이 속한 세대 ID
    private Long householdId;

    // 입주 또는 퇴거한 입주민 ID
    private Long userId;

    // 입주민 이름 (user 테이블 JOIN 결과)
    private String userName;

    // 변경 상태: '입주' 또는 '퇴거'
    private String status;

    // 이력 등록 일시
    private String changedAt;
}