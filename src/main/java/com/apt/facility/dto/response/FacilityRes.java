package com.apt.facility.dto.response;

import com.apt.facility.model.Facility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 시설 응답 DTO | API-048, 049, 050, 051
 * 담당자: 손지혜
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityRes {

    /** 시설 ID */
    private Long facilityId;

    /** 시설 타입 ID */
    private Long typeId;

    /** 시설 타입명 */
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

    /** 운영 여부 */
    private boolean active;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** Facility → FacilityRes 변환 */
    public static FacilityRes of(Facility f) {
        return FacilityRes.builder()
                .facilityId(f.getFacilityId())
                .typeId(f.getTypeId())
                .typeName(f.getTypeName())
                .name(f.getName())
                .description(f.getDescription())
                .maxCapacity(f.getMaxCapacity())
                .price(f.getPrice())
                .openTime(f.getOpenTime())
                .closeTime(f.getCloseTime())
                .slotDuration(f.getSlotDuration())
                .active(f.isActive())
                .createdAt(f.getCreatedAt())
                .build();
    }
}