package com.apt.visitorvehicle.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 내 방문차량 목록 조회 필터 + 페이징 DTO (API-031)
 * GET /api/visitor-vehicles/my 에서 @ModelAttribute로 바인딩
 */
@Getter
@Setter
public class VisitorVehicleGetReq {
    private String licensePlate; // 차량번호 검색 (선택)
    private String visitorName; // 방문자 이름 검색 (선택)
    private LocalDate visitDate; // 방문 예정일 필터 (선택)
    private int page = 1; // 현재 페이지 (기본 1)
    private int size = 10; // 페이지당 개수 (기본 10)
    private int startIdx; // MyBatis용 OFFSET 값

    // page 세팅 시 startIdx 자동 계산
    public void setPage(int page) {
        this.page = page;
        this.startIdx = (page - 1) * this.size;
    }

    // size 세팅 시 startIdx 자동 계산
    public void setSize(int size) {
        this.size = size;
        this.startIdx = (this.page - 1) * size;
    }
}