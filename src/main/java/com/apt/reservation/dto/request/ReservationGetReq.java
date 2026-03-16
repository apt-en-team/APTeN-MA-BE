package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationGetReq {
    private long userId;
    private String facilityName; // 시설 이름 필터
    private String status;       // 상태 필터
    private int page    = 1;
    private int size    = 10;
    private int startIdx;         // MyBatis용 (page-1)*size

    public void setPage(int page) {
        this.page     = page;
        this.startIdx = (page - 1) * this.size;
    }
    public void setSize(int size) {
        this.size     = size;
        this.startIdx = (this.page - 1) * size;
    }
}
