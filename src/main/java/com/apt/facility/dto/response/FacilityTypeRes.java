package com.apt.facility.dto.response;


import com.apt.facility.model.FacilityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** 시설 타입 응답 DTO | API-044, 045, 046 */
@Getter
@Setter
@NoArgsConstructor

public class FacilityTypeRes {

    // 타입 ID
    private long typeId;

    // 타입 명
    private String name;

    // 타입 설명
    private String description;

    // 등록 일시
    private LocalDateTime createdAt;

    public static FacilityTypeRes of(FacilityType t) {
        FacilityTypeRes res = new FacilityTypeRes();
        res.typeId      = t.getTypeId();
        res.name        = t.getName();
        res.description = t.getDescription();
        res.createdAt   = t.getCreatedAt();
        return res;
    }
}
