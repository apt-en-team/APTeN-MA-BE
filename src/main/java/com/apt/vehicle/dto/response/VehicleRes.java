package com.apt.vehicle.dto.response;

import com.apt.vehicle.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 차량 단건 응답 DTO
 * - API-038 목록 조회 → ofList()     : vehicle_id, license_plate, car_model, status, created_at
 * - API-039 등록      → ofRegister() : vehicle_id, user_id, license_plate, car_model, status, created_at
 * - API-040 수정      → ofUpdate()   : vehicle_id, license_plate, car_model
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 JSON 응답 제외
public class VehicleRes {

    /** 차량 ID */
    private Long vehicleId;

    /** 등록자 ID (API-039 등록 응답에만 포함) */
    private Long userId;

    /** 차량 번호판 */
    private String licensePlate;

    /** 차종 */
    private String carModel;

    /** 승인 상태 (PENDING / APPROVED / REJECTED) */
    private String status;

    /** 등록 일시 */
    private LocalDateTime createdAt;

    /** API-038 내 차량 목록 응답용 */
    public static VehicleRes ofList(Vehicle v) {
        return VehicleRes.builder()
                .vehicleId(v.getVehicleId())
                .licensePlate(v.getLicensePlate())
                .carModel(v.getCarModel())
                .status(v.getStatus())        // Model은 String 그대로
                .createdAt(v.getCreatedAt())
                .build();
    }

    /** API-039 차량 등록 응답용 (userId 포함) */
    public static VehicleRes ofRegister(Vehicle v) {
        return VehicleRes.builder()
                .vehicleId(v.getVehicleId())
                .userId(v.getUserId())         // Model 필드 직접 접근
                .licensePlate(v.getLicensePlate())
                .carModel(v.getCarModel())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt())
                .build();
    }

    /** API-040 차량 수정 응답용 (vehicle_id, license_plate, car_model 만) */
    public static VehicleRes ofUpdate(Vehicle v) {
        return VehicleRes.builder()
                .vehicleId(v.getVehicleId())
                .licensePlate(v.getLicensePlate())
                .carModel(v.getCarModel())
                .build();
    }
}