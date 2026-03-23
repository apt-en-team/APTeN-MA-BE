package com.apt.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 시설 타입 등록/수정 요청 DTO | API-045, 046 */
@Getter
@Setter
@NoArgsConstructor
public class FacilityTypeReq {

    // 타입명 (헬스장, 골프장 등)
    @NotBlank
    private String name;

    // 타입 설명
    private String description;
}