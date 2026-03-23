package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// API-035 | 입출차 기록 목록 응답 래퍼 DTO
@Getter
@Setter
public class ParkingLogListRes {
    private List<ParkingLogRes> content;
    private int page;
    private int totalPages;
    private int totalCount;
}