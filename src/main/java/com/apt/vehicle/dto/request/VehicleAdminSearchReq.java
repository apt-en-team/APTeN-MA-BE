package com.apt.vehicle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleAdminSearchReq {

    /** 세대 ID 필터 */
    private Long householdId;

    /** 승인 상태 필터 (PENDING / APPROVED / REJECTED) */
    private String status;

    /** 차종 필터 */
    private String carType;

    /** 동 필터 ← 추가 */
    private String dong;

    /** 페이지 번호 (기본값 0) */
    private int page = 0;

    /** 페이지 크기 (기본값 10) */
    private int size = 10;

    /** 검색어 (차량번호, 차종) */
    private String search;
}