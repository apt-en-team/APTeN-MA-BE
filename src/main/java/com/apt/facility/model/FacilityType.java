package com.apt.facility.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class FacilityType {
    private Long typeId;
    private String name;
    private String description;
    private String category; // 편의시설/GX
    private LocalDateTime createdAt;
}
