package com.apt.household.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseholdGetReq {
    private int page;
    private int size;
    private String dong;
    private String ho;
    private String status;
    private int startIdx;

    // Spring MVC 파라미터 바인딩용 기본 생성자 필수!
    public HouseholdGetReq() {}

    // startIdx 는 setter 로 계산
    public void setPage(int page) {
        this.page = page;
        this.startIdx = (page - 1) * this.size;
    }

    public void setSize(int size) {
        this.size = size;
        this.startIdx = (this.page - 1) * size;
    }
}
