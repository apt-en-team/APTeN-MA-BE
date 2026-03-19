package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardFacilitySummaryRes {
    private Long facilityId;
    private String facilityName;
    private String hours;      // 06:00~22:00 · 정원 30명
    private int currentCount;  // 오늘 예약 건수
    private int totalCount;    // 전체 가능 수
    private int percent;       // current / total * 100
    private String barColor;   // dark / green / yellow
    private boolean isFull;    // 만석 여부
    private Long typeId;

}
