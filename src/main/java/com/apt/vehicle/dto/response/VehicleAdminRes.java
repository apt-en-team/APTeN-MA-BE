package com.apt.vehicle.dto.response;

import com.apt.vehicle.model.Vehicle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/** 전체 차량 목록 응답 DTO (ADMIN) | API-042 GET /api/admin/vehicles */
@Getter
@Builder
public class VehicleAdminRes {

    /** 차량 ID */
    private Long vehicleId;

    /** 소속 세대 정보 (동/호) */
    private HouseholdInfo household;

    /** 등록자 정보 (이름) */
    private UserInfo user;

    /** 차량 번호판 */
    private String licensePlate;

    /** 차종 */
    private String carModel;

    /** 승인 상태 (PENDING / APPROVED / REJECTED) */
    private String status;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** 세대 정보 중첩 DTO */
    @Getter
    @Builder
    public static class HouseholdInfo {
        /** 동 */
        private String dong;
        /** 호 */
        private String ho;
    }

    /** 등록자 정보 중첩 DTO */
    @Getter
    @Builder
    public static class UserInfo {
        /** 등록자 이름 */
        private String name;
    }

    /** Vehicle Model → VehicleAdminRes 변환 */
    public static VehicleAdminRes of(Vehicle v) {
        return VehicleAdminRes.builder()
                .vehicleId(v.getVehicleId())
                .household(HouseholdInfo.builder()
                        .dong(v.getDong())       // Model 필드 직접 접근
                        .ho(v.getHo())
                        .build())
                .user(UserInfo.builder()
                        .name(v.getUserName())   // Model 필드 직접 접근
                        .build())
                .licensePlate(v.getLicensePlate())
                .carModel(v.getCarModel())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt())
                .build();
    }
}