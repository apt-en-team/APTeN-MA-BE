package com.apt.vehicle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VehicleReq {

    private Long householdId;
    private Long approvedBy;
    private String licensePlate;  // 차량 번호판
    private String carModel;      // 차량 모델명
}