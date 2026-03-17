package com.apt.vehicle.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 차량 수정 요청 DTO | API-040 PUT /api/vehicles/{id} */
@Getter
@NoArgsConstructor
public class VehicleUpdateReq {

    /** 차량 번호판 (수정 가능) */
    @Pattern(
            regexp = "^[0-9]{2,3}[가-힣]\\s?[0-9]{4}$",
            message = "차량번호 형식이 올바르지 않습니다. (예: 12가 3456)"
    )
    @Size(max = 20)
    private String licensePlate;

    /** 차종 */
    @Size(max = 50, message = "차종은 50자 이내여야 합니다.")
    private String carModel;
}