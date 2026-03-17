package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminVisitorVehicleRes {
    private Long visitorVehicleId; // 방문차량 PK
    private String licensePlate; // 차량번호
    private String visitorName;  // 방문자 이름
    private String visitPurpose; // 방문 목적
    private LocalDate visitDate; // 방문 예정일
    private String status; // 상태 (APPROVED, CANCELLED 등)
    private String userName; // 등록한 입주민 이름 (user JOIN)
    private String dong; // 동 (user JOIN)
    private String ho; // 호 (user JOIN)
    private boolean isEntered; // 입차 여부 (추후 parking_log 연동)
}

