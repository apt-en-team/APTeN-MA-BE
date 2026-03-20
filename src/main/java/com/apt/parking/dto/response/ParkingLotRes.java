package com.apt.parking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 주차장 정보 수정 응답 DTO
// current_count는 parking_log 집계로 계산하므로 포함하지 않음
// 현재 주차 현황은 /parking/status API에서 별도 조회
@Getter
@AllArgsConstructor
public class ParkingLotRes {
    private Long lotId; // 주차장 ID
    private String name; // 주차장 이름
    private int totalSpaces; // 전체 주차 면수
}