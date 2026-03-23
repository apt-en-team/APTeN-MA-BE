package com.apt.notification.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 알림 조회 API 응답 DTO
// Notification 모델에서 프론트에 필요한 필드만 담아서 반환한다
// (userId는 이미 로그인된 사용자이므로 응답에서 제외)
@Getter
@Setter
public class NotificationRes {

    // 알림 고유 ID (읽음 처리 시 이 ID를 사용)
    private Long notificationId;

    // 알림 종류 (프론트에서 아이콘 분기 처리 등에 사용)
    private String type;

    // 알림 내용
    private String message;

    // 읽음 여부
    private boolean isRead;

    // 알림 생성 시각 (프론트에서 "몇 분 전" 등으로 표시)
    private java.time.LocalDateTime createdAt;
}