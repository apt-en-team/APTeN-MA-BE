package com.apt.vehicle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** 차량 엔티티 | vehicle 테이블 매핑 */
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    /** 차량 ID (PK) */
    private Long vehicleId;

    /** 등록자 ID (FK → user) */
    private Long userId;

    /** 세대 ID (FK → household) */
    private Long householdId;

    /** 세대 동 */
    private String dong;

    /** 세대 호 */
    private String ho;

    /** 등록자 이름 */
    private String userName;

    /** 차량 번호판 */
    private String licensePlate;

    /** 차량모델 */
    private String carModel;

    /** 차종 */
    private String carType;

    /** 승인 상태 (PENDING / APPROVED / REJECTED) */
    private String status;

    /** 승인자 ID (FK → user) */
    private Long approvedBy;

    /** 승인 일시 */
    private LocalDateTime approvedAt;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** 삭제 일시 (소프트 딜리트) */
    private LocalDateTime deletedAt;
}