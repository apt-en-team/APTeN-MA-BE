package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingStatusRes {
    private int totalSpaces;      // 전체 주차 면수
    private int currentCount;     // 현재 주차 대수 (IN - OUT)
    private int availableCount;   // 남은 자리
    private int registeredCount;  // 등록차량 현재 주차 수
    private int visitorCount;     // 방문차량 현재 주차 수
    private int unregisteredCount; // 미등록차량 현재 주차 수
}