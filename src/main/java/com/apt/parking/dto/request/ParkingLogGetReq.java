package com.apt.parking.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// API-035 | 입출차 기록 조회 요청 DTO
// GET /api/parking/logs?licensePlate=...&startDate=...&endDate=...&page=1&size=10
@Getter
@Setter
public class ParkingLogGetReq {
    private String licensePlate;   // 차량번호 검색 (선택)
    private LocalDate startDate;   // 조회 시작일 (선택)
    private LocalDate endDate;     // 조회 종료일 (선택)
    private String entryType;   // IN | OUT
    private String vehicleType; // 등록차량 | 방문차량 | 고정방문차량 | 미등록차량
    private int page = 1;
    private int size = 10;
    private int startIdx;          // MyBatis용 OFFSET 값

    public void setPage(int page) {
        this.page = page;
        this.startIdx = (page - 1) * this.size;
    }

    public void setSize(int size) {
        this.size = size;
        this.startIdx = (this.page - 1) * size;
    }
}