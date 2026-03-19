package com.apt.vehicle.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 차량 수정 요청 DTO | API-040 PUT /api/vehicles/{id} */
@Getter
@NoArgsConstructor
public class VehicleUpdateReq {

    /** 차량 번호판 (선택) */
    @Size(max = 20, message = "차량 번호는 20자 이내여야 합니다.")
    private String licensePlate;

    /** 차종 (선택) */
    @Size(max = 50, message = "차종은 50자 이내여야 합니다.")
    private String carModel;
}