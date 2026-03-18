package com.apt.facility.dto.response;

import com.apt.facility.model.FacilityType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 시설 타입 응답 DTO | API-044, 045, 046
 * 담당자: 손지혜
 */
@Getter
@Builder
public class FacilityTypeRes {

    /** 시설 타입 ID */
    private Long typeId;

    /** 타입명 */
    private String name;

    /** 타입 설명 */
    private String description;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** FacilityType → FacilityTypeRes 변환 */
    public static FacilityTypeRes of(FacilityType t) {
        return FacilityTypeRes.builder()
                .typeId(t.getTypeId())
                .name(t.getName())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .build();
    }
}