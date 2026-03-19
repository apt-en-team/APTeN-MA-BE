package com.apt.vehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 차량 등록 요청 DTO
 * API-039 POST /api/vehicles 요청 시 사용됨
 */
@Getter
@NoArgsConstructor
public class VehicleReq {

    /** * 차량 번호판 (필수 필드)
     * 정규식 설명: 숫자 2~3자리 + 한글 1자리 + 선택적 공백(\\s*) + 숫자 4자리
     * 예시: "12가 3456", "371소6530" 모두 허용
     */
    @NotBlank(message = "차량번호는 필수입니다.")
    @Pattern(
            regexp = "^[0-9]{2,3}[가-힣]\\s*[0-9]{4}$",
            message = "차량번호 형식이 올바르지 않습니다. (예: 12가 3456)"
    )
    @Size(max = 20, message = "차량번호는 20자 이내여야 합니다.")
    private String licensePlate;

    /** * 차종 모델 (선택 필드)
     * 예시: "현대 아반떼", "테슬라 모델3"
     */
    @Size(max = 50, message = "차종 모델명은 50자 이내여야 합니다.")
    private String carModel;

    /** * 차량 타입 (선택 필드)
     * 예시: "승용차", "SUV", "전기차" 등 프론트 전송 값 대응
     */
    @Size(max = 20, message = "차량 타입은 20자 이내여야 합니다.")
    private String carType;
}