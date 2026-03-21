package com.apt.facility.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class FacilityListTypeRes {
    private Long typeId;
    private String typeName;
    private LocalDateTime createdAt;
    private String category; // 편의시설/GX

    private Long facilityId;
    private String facilityName;
    private int maxCapacity;
    private LocalTime openTime;
    private LocalTime closeTime;
}
