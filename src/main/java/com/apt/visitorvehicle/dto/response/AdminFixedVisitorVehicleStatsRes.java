package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminFixedVisitorVehicleStatsRes {
    private int totalCount; // 전체 등록 건수
    private int unlimitedCount; // 무기한 차량 수
    private int monthCount; // 이번 달 등록 건수
    private int activeCount; // 활성 차량 수
}
