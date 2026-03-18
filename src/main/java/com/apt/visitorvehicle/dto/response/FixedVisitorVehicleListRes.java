package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FixedVisitorVehicleListRes {
    private List<FixedVisitorVehicleRes> content;
    private int page;
    private int totalPages;
    private int totalCount;
    // 사이드 카드 통계 (필터 무관)
    private int allCount;
    private int unlimitedCount;
}