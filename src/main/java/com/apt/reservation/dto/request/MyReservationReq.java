package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReservationReq {
    private int page;        // 현재페이지
    private int size;        // 페이지사이즈
    private String tab;      // UPCOMING / PAST
    private String keyword;  // 시설검색
    private long userId;

    private int startIdx;    // limit용

    public void setPage(int page) {
        this.page = page;
        this.startIdx = (page - 1) * this.size;
    }

    public void setSize(int size) {
        this.size = size;
        this.startIdx = (this.page - 1) * size;
    }
}
