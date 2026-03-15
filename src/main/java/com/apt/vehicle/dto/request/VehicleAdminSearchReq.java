package com.apt.vehicle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 전체 차량 목록 조회 요청 DTO | API-042 GET /api/admin/vehicles */
@Getter
@Setter
@NoArgsConstructor
public class VehicleAdminSearchReq {

    /** 세대 ID 필터 (선택, null 이면 전체 조회) */
    private Long householdId;

    /** 페이지 번호 (기본값 0) */
    private int page = 0;

    /** 페이지 크기 (기본값 10) */
    private int size = 10;
}