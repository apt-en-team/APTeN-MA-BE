package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VisitorVehicleRes {
    private Long visitorVehicleId;   // 생성된 방문차량 ID
    private Long userId;             // 등록자 ID
    private String licensePlate;     // 차량번호
    private String visitorName;      // 방문자 이름
    private LocalDate visitDate;     // 방문 예정일
    private String visitPurpose;     // 방문 목적
    private String status;           // 승인 상태
    private LocalDateTime createdAt; // 등록 일시
}