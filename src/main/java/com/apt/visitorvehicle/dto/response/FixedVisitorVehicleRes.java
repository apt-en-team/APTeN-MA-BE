package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// API-062 | 고정 방문차량 등록 응답 DTO
@Getter
@Setter
public class FixedVisitorVehicleRes {
    private Long fixedId;              // 고정차량 PK
    private String vehicleNumber;      // 차량번호
    private String visitorName;        // 방문자 이름
    private String purpose;            // 방문 목적
    private LocalDate startDate;       // 고정 시작일
    private LocalDate endDate;         // 고정 종료일
    private LocalDateTime createdAt;   // 등록 일시

    // 관리자 목록 조회 시 JOIN으로 채워짐 (입주민 조회 시 null)
    private String userName;
    private String dong;
    private String ho;
    private int isDeleted;
}
