package com.apt.vehicle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    private Long vehicleId;
    private Long userId;
    private Long householdId;
    private String licensePlate;
    private String carModel;
    private String status;        // PENDING, APPROVED, REJECTED
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}