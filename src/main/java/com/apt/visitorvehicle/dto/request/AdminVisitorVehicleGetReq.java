package com.apt.visitorvehicle.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminVisitorVehicleGetReq {
    private LocalDate visitDate;
    private String licensePlate;
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

