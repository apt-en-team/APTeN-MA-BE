package com.apt.visitorvehicle.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// fixed_visitor_vehicle 테이블 매핑
@Getter
@Setter
public class FixedVisitorVehicle {
    private Long fixedId;              // PK (AUTO_INCREMENT)
    private Long userId;               // 등록자 ID (FK → user)
    private String vehicleNumber;      // 차량번호
    private String visitorName;        // 방문자 이름
    private String purpose;            // 방문 목적
    private LocalDate startDate;       // 고정 시작일
    private LocalDate endDate;         // 고정 종료일 (NULL이면 무기한)
    private LocalDateTime createdAt;   // 등록 일시
    private int isDeleted;
    private LocalDateTime deletedAt;
}
