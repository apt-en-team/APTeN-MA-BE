package com.apt.vehicle.dto.response;

import com.apt.vehicle.model.Vehicle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class VehicleRes {

    private Long vehicleId;
    private Long userId;
    private String licensePlate;
    private String carModel;
    private String status;         // PENDING, APPROVED, REJECTED
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;

    // Vehicle model → VehicleRes 변환 생성자
    public VehicleRes(Vehicle vehicle) {
        this.vehicleId = vehicle.getVehicleId();
        this.userId = vehicle.getUserId();
        this.licensePlate = vehicle.getLicensePlate();
        this.carModel = vehicle.getCarModel();
        this.status = vehicle.getStatus();
        this.approvedAt = vehicle.getApprovedAt();
        this.createdAt = vehicle.getCreatedAt();
    }
}