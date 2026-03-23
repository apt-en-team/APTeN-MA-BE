package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// API-034, API-035 | 입출차 기록 응답 DTO
@Getter
@Setter
public class ParkingLogRes {
    private Long logId; // 기록 PK
    private String licensePlate; // 차량번호
    private String entryType; // 입출차 구분 (IN / OUT)
    private Long vehicleId; // 등록차량 FK (null이면 미등록)
    private Long visitorVehicleId; // 방문차량 FK (null이면 방문차량 아님)
    private Long fixedVisitorVehicleId;  // 고정방문차량 FK (null이면 고정방문차량 아님)
    private LocalDateTime loggedAt; // 입출차 일시

    // 차량 자동 판별 결과 — 프론트 화면에 표시용
    // "등록차량", "방문차량", "고정방문차량", "미등록차량" 중 하나
    private String vehicleType;

    // 판별된 차량의 상세 정보 (등록차량이면 "101동 101호 홍길동", 방문차량이면 방문자 이름 등)
    private String vehicleInfo;
}