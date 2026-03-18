package com.apt.facility.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 시설 엔티티 | facility 테이블 매핑
 * 담당자: 손지혜
 */
@Getter
@Setter
@NoArgsConstructor
public class Facility {

    /** 시설 ID (PK) */
    private Long facilityId;

    /** 시설 타입 ID (FK → facility_type) */
    private Long typeId;

    /** 시설 타입명 (JOIN) */
    private String typeName;

    /** 시설명 */
    private String name;

    /** 시설 설명 */
    private String description;

    /** 최대 수용 인원 */
    private Integer maxCapacity;

    /** 월수강료 */
    private Integer price;

    /** 운영 시작 시간 */
    private LocalTime openTime;

    /** 운영 종료 시간 */
    private LocalTime closeTime;

    /** 예약 단위 (분) */
    private Integer slotDuration;

    /** 운영 여부 (true: 운영, false: 중단) */
    private boolean active;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** 삭제 일시 (소프트 딜리트) */
    private LocalDateTime deletedAt;
}