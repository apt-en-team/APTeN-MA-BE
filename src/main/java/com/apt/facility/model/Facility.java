package com.apt.facility.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * facility 테이블 매핑 모델
 * 담당: 손지혜
 */
@Getter
@Setter
public class Facility {

    /** 시설 ID (PK) */
    private Long facilityId;

    /** 시설 타입 ID (FK → facility_type.type_id) */
    private Long typeId;

    /** 시설명 (예: GX(오전), 독서실(남)) */
    private String name;

    /** 시설 설명 */
    private String description;

    /** 시간대별 최대 수용 인원 */
    private int maxCapacity;

    /** 운영 시작 시간 */
    private LocalTime openTime;

    /** 운영 종료 시간 */
    private LocalTime closeTime;

    /** 예약 단위 (분) - 기본값 60 */
    private int slotDuration;

    /** 운영 여부 (1: 운영, 0: 중단) */
    private boolean isActive;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    private int price;

}
