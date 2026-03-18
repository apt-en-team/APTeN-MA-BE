package com.apt.vehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleAdminRegisterReq {

    /** 등록할 유저 ID */
    @NotNull(message = "입주민을 선택해주세요")
    private Long userId;

    /** 차량 번호 */
    @NotBlank(message = "차량 번호를 입력해주세요")
    private String licensePlate;

    /** 차 모델 */
    private String carModel;

    /** 차종 */
    private String carType;

    /** 초기 승인 상태 (PENDING/APPROVED/REJECTED) */
    @NotBlank(message = "승인 상태를 선택해주세요")
    private String status;
}