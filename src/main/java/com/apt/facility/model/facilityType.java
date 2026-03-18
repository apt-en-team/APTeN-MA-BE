package com.apt.facility.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class facilityType {
    private Long typeId;
    private String name;
    private String description;
    private LocalDateTime created_at;
}
