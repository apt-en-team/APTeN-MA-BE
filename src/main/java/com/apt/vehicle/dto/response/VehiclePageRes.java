package com.apt.vehicle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** 차량 목록 페이지 응답 래퍼 DTO | API-042 GET /api/admin/vehicles */
@Getter
@AllArgsConstructor
public class VehiclePageRes<T> {

    /** 차량 목록 */
    private List<T> content;

    /** 현재 페이지 번호 (0부터 시작) */
    private int page;

    /** 전체 페이지 수 */
    private int totalPages;
}