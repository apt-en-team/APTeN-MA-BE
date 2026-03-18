package com.apt.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 시설 타입 등록/수정 요청 DTO | API-045, 046
 * 담당자: 손지혜
 */
@Getter
@Setter
@NoArgsConstructor
public class FacilityTypeReq {

    /** 타입명 (필수) */
    @NotBlank(message = "타입명은 필수입니다.")
    private String name;

    /** 타입 설명 (선택) */
    private String description;
}