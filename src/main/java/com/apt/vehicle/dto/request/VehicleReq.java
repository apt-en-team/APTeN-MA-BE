package com.apt.vehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 차량 등록 요청 DTO | API-039 POST /api/vehicles */
@Getter
@NoArgsConstructor
public class VehicleReq {

    /** 차량 번호판 (필수, 형식: 12가3456) */
    @NotBlank(message = "차량번호는 필수입니다.")
    @Pattern(
            regexp = "^[0-9]{2,3}[가-힣]\\s?[0-9]{4}$",
            message = "차량번호 형식이 올바르지 않습니다. (예: 12가 3456)"
    )
    @Size(max = 20, message = "차량번호는 20자 이내여야 합니다.")
    private String licensePlate;

    /** 차종 (선택, 예: 소나타) */
    @Size(max = 50, message = "차종은 50자 이내여야 합니다.")
    private String carModel;

    /** 차량 종류 (선택) */
    @Size(max = 20)
    private String carType;

}