package com.apt.notification.mapper;

import com.apt.notification.dto.response.NotificationRes;
import com.apt.notification.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// notification 테이블에 접근하는 MyBatis Mapper
// 실제 SQL은 NotificationMapper.xml 에 작성되어 있다
@Mapper
public interface NotificationMapper {

    // 알림 1건 저장 (NotificationService에서 호출)
    void insert(Notification notification);

    // 특정 유저의 알림 목록 조회 (최신순 30건)
    List<NotificationRes> findByUserId(@Param("userId") Long userId);

    // 읽지 않은 알림 개수 조회 (벨 아이콘 뱃지 숫자에 사용)
    int countUnread(@Param("userId") Long userId);

    // 특정 알림 1건 읽음 처리 (userId 조건 포함 → 본인 알림만 처리 가능)
    void markAsRead(@Param("notificationId") Long notificationId,
                    @Param("userId") Long userId);

    // 해당 유저의 모든 알림 읽음 처리
    void markAllAsRead(@Param("userId") Long userId);

    // ADMIN role 유저의 ID 목록 조회
    // 새 회원가입/차량등록 신청 시 관리자 전원에게 알림을 보내기 위해 사용
    List<Long> findAdminUserIds();
}