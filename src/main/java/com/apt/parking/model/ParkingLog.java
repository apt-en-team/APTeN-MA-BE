package com.apt.parking.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// parking_log 테이블 매핑
@Getter
@Setter
public class ParkingLog {
    private Long logId;                  // PK (AUTO_INCREMENT)
    private String licensePlate;         // 차량번호
    private String entryType;            // 입출차 구분 (IN / OUT)
    private Long vehicleId;              // 등록차량 FK (미등록이면 null)
    private Long visitorVehicleId;       // 방문차량 FK (방문차량 아니면 null)
    private Long fixedVisitorVehicleId;  // 고정방문차량 FK (고정방문차량 아니면 null)
    private LocalDateTime loggedAt;      // 입출차 일시
}