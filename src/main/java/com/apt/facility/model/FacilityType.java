package com.apt.facility.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** 시설 타입 엔티티 | facility_type 테이블 매핑 */
@Getter
@Setter
@NoArgsConstructor

public class FacilityType {

    /** 타입 ID (PK) */
    private Long typeId;

    /** 타입명 (헬스장, 골프장 등) */
    private String name;

    /** 타입 설명 */
    private String description;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** 삭제 일시 (소프트 딜리트) */
    private LocalDateTime deletedAt;
}