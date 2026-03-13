package com.apt.visitorvehicle.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VisitorVehicle {
    private Long visitorVehicleId;   // 방문차량 PK
    private Long userId;             // 등록한 입주민 ID (FK → user)
    private String licensePlate;     // 차량번호
    private String visitorName;      // 방문자 이름
    private String visitPurpose;     // 방문 목적
    private LocalDate visitDate;     // 방문 예정일
    private String status;           // 승인 상태 (APPROVED / REJECTED / CANCELLED)
    private LocalDateTime createdAt; // 등록 일시

    // API-034 | 수정
    public void update(String licensePlate, String visitorName,
                       String visitPurpose, LocalDate visitDate) {
        this.licensePlate = licensePlate;
        this.visitorName = visitorName;
        this.visitPurpose = visitPurpose;
        this.visitDate = visitDate;
    }

    // API-036 | 등록 취소
    public void cancel() {
        this.status = "CANCELLED";
    }
}