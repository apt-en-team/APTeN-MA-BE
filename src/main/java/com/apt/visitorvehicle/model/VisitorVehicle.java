package com.apt.visitorvehicle.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VisitorVehicle {
    private Long visitorVehicleId;  // 방문차량 PK
    private Long userId;            // 등록한 입주민 ID (FK → user)
    private String licensePlate;    // 차량번호
    private String visitorName;     // 방문자 이름
    private String visitPurpose;    // 방문 목적
    private LocalDate visitDate;    // 방문 예정일
    private String status;          // 승인 상태 (APPROVED / REJECTED)
    private LocalDateTime createdAt; // 등록 일시
}