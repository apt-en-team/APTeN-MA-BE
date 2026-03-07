package com.apt.household.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// DB household_history 테이블과 매핑되는 Entity 클래스
// 세대별 입주/퇴거 이력을 누적 관리 (삭제 없음)
// 현재 입주 상태는 가장 최근 이력의 status 값으로 판단
@Getter
@Setter
public class HouseholdHistory {

    // 이력 고유 ID (PK, AUTO_INCREMENT)
    private Long historyId;

    // 이력이 속한 세대 ID (FK → household.household_id)
    private Long householdId;

    // 입주 또는 퇴거한 입주민 ID (FK → user.user_id)
    private Long userId;

    // 변경 상태: '입주' 또는 '퇴거' (DB ENUM)
    private String status;

    // 이력 등록 일시
    private LocalDateTime changedAt;
}
