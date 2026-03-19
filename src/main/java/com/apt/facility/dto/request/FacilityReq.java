package com.apt.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 시설 등록/수정 요청 DTO | API-050, 051 */
@Getter @Setter @NoArgsConstructor
public class FacilityReq {

    /** 시설 타입 ID */
    @NotNull
    private Long typeId;

    /** 시설명 */
    @NotBlank
    private String name;

    /** 시설 설명 */
    private String description;

    /** 최대 정원 */
    @NotNull
    private Integer maxCapacity;

    /** 운영 시작 시간 (HH:mm) */
    @NotBlank
    private String openTime;

    /** 운영 종료 시간 (HH:mm) */
    @NotBlank
    private String closeTime;

    /** 예약 단위 (분, 기본 60) */
    private int slotDuration = 60;

    /** 운영 여부 (기본 true) */
    private boolean isActive = true;
}