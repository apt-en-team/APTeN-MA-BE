package com.apt.parking.dto.request;

import lombok.Getter;

@Getter
public class ParkingLotUpdateReq {
    private String name;// 주차장 이름
    private int totalSpaces; // 전체 주차 면수
}