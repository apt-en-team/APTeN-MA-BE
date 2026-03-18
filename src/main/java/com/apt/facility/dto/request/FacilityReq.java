package com.apt.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 시설 등록/수정 요청 DTO | API-050, 051
 * 담당자: 손지혜
 */
@Getter
@Setter
@NoArgsConstructor
public class FacilityReq {

    /** 시설 타입 ID (필수) */
    @NotNull(message = "시설 타입을 선택해주세요.")
    private Long typeId;

    /** 시설명 (필수) */
    @NotBlank(message = "시설명은 필수입니다.")
    private String name;

    /** 시설 설명 (선택) */
    private String description;

    /** 최대 수용 인원 (필수) */
    @NotNull(message = "최대 수용 인원은 필수입니다.")
    private Integer maxCapacity;

    /** 월수강료 (기본 0) */
    private int price = 0;

    /** 운영 시작 시간 HH:mm (필수) */
    @NotBlank(message = "운영 시작 시간은 필수입니다.")
    private String openTime;

    /** 운영 종료 시간 HH:mm (필수) */
    @NotBlank(message = "운영 종료 시간은 필수입니다.")
    private String closeTime;

    /** 예약 단위 분 (기본 60) */
    private int slotDuration = 60;

    /** 운영 여부 (기본 true) */
    private boolean active = true;
}