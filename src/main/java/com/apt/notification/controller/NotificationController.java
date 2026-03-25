package com.apt.notification.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.notification.dto.response.NotificationRes;
import com.apt.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 알림 API 컨트롤러
// 로그인한 사용자 본인의 알림만 조회/처리할 수 있다
// @AuthenticationPrincipal 로 현재 로그인한 유저의 userId를 가져온다
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/notifications
    // 내 알림 목록 조회 (최신순 30건)
    @GetMapping
    public ResponseEntity<ResultResponse<List<NotificationRes>>> getNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<NotificationRes> list = notificationService.getNotifications(principal.getUserId());
        return ResponseEntity.ok(ResultResponse.success("알림 조회 성공", list));
    }

    // GET /api/notifications/unread-count
    // 읽지 않은 알림 개수 조회 (벨 아이콘 뱃지 숫자에 사용)
    @GetMapping("/unread-count")
    public ResponseEntity<ResultResponse<Integer>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal principal) {
        int count = notificationService.getUnreadCount(principal.getUserId());
        return ResponseEntity.ok(ResultResponse.success("읽지 않은 알림 수 조회 성공", count));
    }

    // PATCH /api/notifications/{notificationId}/read
    // 알림 1건 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ResultResponse<Void>> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAsRead(notificationId, principal.getUserId());
        return ResponseEntity.ok(ResultResponse.success("읽음 처리 성공", null));
    }

    // PATCH /api/notifications/read-all
    // 내 알림 전체 읽음 처리 (벨 드롭다운에서 "모두 읽음" 버튼 클릭 시)
    @PatchMapping("/read-all")
    public ResponseEntity<ResultResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAllAsRead(principal.getUserId());
        return ResponseEntity.ok(ResultResponse.success("전체 읽음 처리 성공", null));
    }
}