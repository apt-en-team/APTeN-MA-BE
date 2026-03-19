package com.apt.parking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// API-034 | 입출차 기록 생성 요청 DTO
@Getter
@Setter
public class ParkingLogReq {
    @NotBlank
    private String licensePlate;  // 차량번호 (필수)

    @NotBlank
    @Pattern(regexp = "^(IN|OUT)$", message = "entryType은 IN 또는 OUT이어야 합니다")
    private String entryType;     // 입출차 구분 (IN / OUT, 필수)
}