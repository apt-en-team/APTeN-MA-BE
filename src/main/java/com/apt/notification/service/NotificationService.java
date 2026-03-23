package com.apt.notification.service;

import com.apt.notification.dto.response.NotificationRes;
import com.apt.notification.mapper.NotificationMapper;
import com.apt.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 알림 비즈니스 로직 처리 클래스
// 1차에서는 관리자에게 보내는 알림만 구현 (입주민 알림은 2차 PWA 때 웹 푸시로 추가 예정)
// 다른 서비스(AuthService, VehicleService)에서 이 서비스를 주입받아 알림을 저장한다
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    // ── 내부 공통 저장 메서드 ─────────────────────────────────────
    // Notification 객체를 만들어 DB에 저장한다
    // 외부에서 직접 호출하지 않고 아래 public 메서드들이 내부적으로 사용
    private void save(Long userId, String type, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setType(type);
        n.setMessage(message);
        notificationMapper.insert(n);
    }

    // ── 관리자에게 보내는 알림 ────────────────────────────────────

    // 새 회원가입 신청 알림 → 관리자 전원에게
    // AuthService.linkHousehold() 에서 호출 (소셜/일반 가입 모두)
    public void notifyNewMemberToAdmins(String userName) {
        // DB에서 ADMIN role 유저 ID 전체 조회 후 각각 알림 INSERT
        List<Long> adminIds = notificationMapper.findAdminUserIds();
        for (Long adminId : adminIds) {
            save(adminId, "NEW_MEMBER", userName + "님이 회원 가입을 신청했습니다.");
        }
    }

    // 차량 등록 신청 알림 → 관리자 전원에게
    // VehicleService.registerVehicle() 에서 호출
    public void notifyNewVehicleToAdmins(String userName, String licensePlate) {
        List<Long> adminIds = notificationMapper.findAdminUserIds();
        for (Long adminId : adminIds) {
            save(adminId, "NEW_VEHICLE", userName + "님이 차량(" + licensePlate + ") 등록을 신청했습니다.");
        }
    }

    // ── 조회 / 읽음 처리 ─────────────────────────────────────────

    // 내 알림 목록 조회 (최신순 30건)
    // NotificationController.getNotifications() 에서 호출
    public List<NotificationRes> getNotifications(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    // 읽지 않은 알림 개수 조회 (벨 아이콘 뱃지 숫자)
    public int getUnreadCount(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    // 알림 1건 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationMapper.markAsRead(notificationId, userId);
    }

    // 내 알림 전체 읽음 처리
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }
}