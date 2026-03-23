package com.apt.notification.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// notification 테이블과 1:1 매핑되는 모델 클래스
// 알림 한 건의 데이터를 담는다
@Getter
@Setter
public class Notification {

    // 알림 고유 ID (PK, AUTO_INCREMENT)
    private Long notificationId;

    // 알림을 받는 사람의 user_id (FK → user)
    private Long userId;

    // 알림 종류
    // NEW_MEMBER  : 새 회원 가입 신청 → 관리자에게
    // NEW_VEHICLE : 차량 등록 신청 → 관리자에게
    private String type;

    // 알림 내용 (예: "홍길동님이 회원 가입을 신청했습니다.")
    private String message;

    // 읽음 여부 (false: 안읽음, true: 읽음)
    private boolean isRead;

    // 알림 생성 시각
    private LocalDateTime createdAt;
}