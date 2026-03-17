package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class VisitorVehicleListRes {
    private List<VisitorVehicleRes> content;
    private int page;
    private int totalPages;
    private int totalCount;
    // 사이드 카드 통계 (필터 무관)
    private int todayCount;
    private int upcomingCount;
    private int allCount;
}