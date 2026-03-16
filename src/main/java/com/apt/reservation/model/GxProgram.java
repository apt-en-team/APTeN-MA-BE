package com.apt.reservation.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class GxProgram {

    private Long programId;
    private Long facilityId;
    private int maxCapacity; // facility JOIN
    private LocalDate startDate;
    private LocalDate endDate;
    private String daysOfWeek;
    private String status; // OPEN / CLOSED
    private LocalDateTime createdAt;

}
