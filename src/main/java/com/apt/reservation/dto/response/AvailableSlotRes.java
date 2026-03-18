package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AvailableSlotRes {

    private LocalTime startTime;
    private LocalTime endTime;
    private int availableCount;   // total - reserved
    private int totalCapacity;
    private int reservedCount;

}
