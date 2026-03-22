package com.apt.facility.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * facility 테이블 매핑 모델
 * 담당: 손지혜
 */
@Getter
@Setter
@NoArgsConstructor
public class Facility {

    /** 시설 ID (PK) */
    private Long facilityId;

    /** 시설 타입 ID (FK → facility_type.type_id) */
    private Long typeId;

    /** 시설 타입명 (JOIN용) */
    private String typeName;

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

    /** 오늘 예약 수 */
    private int todayReserved;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** 수강료 */
    private int price;

    /** 현재 타임 예약 수 */
    private int currentSlotReserved;

    /** 삭제 일시 (소프트 딜리트) */
    private LocalDateTime deletedAt;

    // MyBatis 매핑용
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    private Long programId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String daysOfWeek;

}