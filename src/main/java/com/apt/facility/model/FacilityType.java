package com.apt.facility.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FacilityType {
    private Long typeId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
