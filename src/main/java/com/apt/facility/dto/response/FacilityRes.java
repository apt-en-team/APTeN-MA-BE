package com.apt.facility.dto.response;

import com.apt.facility.model.Facility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/** 시설 응답 DTO | API-048, 049, 050, 051 */
@Getter
@Setter
@NoArgsConstructor
public class FacilityRes {

    // 시설 ID
    private Long facilityId;

    // 시설 타입 ID
    private Long typeId;

    // 시설 타입명
    private String typeName;

    // 시설명
    private String name;

    // 시설 설명
    private String description;

    // 최대 정원
    private int maxCapacity;

    // 운영 시작 시간
    private LocalTime openTime;

    // 운영 종료 시간
    private LocalTime closeTime;

    // 예약 단위 (분)
    private int slotDuration;

    // 운영 여부
    private Boolean isActive;

    // 필드 추가
    private int todayReserved;

    // 사용료
    private int price;

    // 등록 일시
    private LocalDateTime createdAt;

    public static FacilityRes of(Facility f) {
        FacilityRes res = new FacilityRes();
        res.facilityId   = f.getFacilityId();
        res.typeId       = f.getTypeId();
        res.typeName     = f.getTypeName();
        res.name         = f.getName();
        res.description  = f.getDescription();
        res.maxCapacity  = f.getMaxCapacity();
        res.openTime     = f.getOpenTime();
        res.closeTime    = f.getCloseTime();
        res.slotDuration = f.getSlotDuration();
        res.isActive     = f.getIsActive();
        res.todayReserved = f.getTodayReserved();
        res.price        = f.getPrice();
        res.createdAt    = f.getCreatedAt();
        return res;
    }
}