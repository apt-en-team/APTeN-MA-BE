package com.apt.vehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.vehicle.dto.request.VehicleReq;
import com.apt.vehicle.dto.response.VehicleRes;
import com.apt.vehicle.mapper.VehicleMapper;
import com.apt.vehicle.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleMapper vehicleMapper;

    // API-038 | 내 차량 목록 조회
    public List<VehicleRes> getMyVehicles(Long userId) {
        List<Vehicle> vehicles = vehicleMapper.findByUserId(userId);
        return vehicles.stream()
                .map(VehicleRes::new)
                .collect(Collectors.toList());
    }

    // API-039 | 차량 등록 (세대당 2대 제한)
    public VehicleRes registerVehicle(VehicleReq req, Long userId) {
        int count = vehicleMapper.countByHouseholdId(req.getHouseholdId());
        if (count >= 2) {
            throw new CustomException(ErrorCode.VEHICLE_LIMIT_EXCEEDED);
        }
        if (vehicleMapper.existsByLicensePlate(req.getLicensePlate()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setHouseholdId(req.getHouseholdId());
        vehicle.setLicensePlate(req.getLicensePlate());
        vehicle.setCarModel(req.getCarModel());
        vehicle.setStatus("PENDING");

        vehicleMapper.insertVehicle(vehicle);
        return new VehicleRes(vehicle);
    }

    // API-040 | 차량 수정
    public VehicleRes updateVehicle(Long vehicleId, VehicleReq req, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        if (!vehicle.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (!vehicle.getLicensePlate().equals(req.getLicensePlate())
                && vehicleMapper.existsByLicensePlate(req.getLicensePlate()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
        }

        vehicle.setLicensePlate(req.getLicensePlate());
        vehicle.setCarModel(req.getCarModel());
        vehicleMapper.updateVehicle(vehicle);

        return new VehicleRes(vehicle);
    }

    // API-041 | 차량 삭제
    public void deleteVehicle(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        if (!vehicle.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        vehicleMapper.deleteVehicle(vehicleId);
    }

    // API-042 | 전체 차량 목록 조회 (관리자)
    public List<VehicleRes> getAllVehicles() {
        List<Vehicle> vehicles = vehicleMapper.findAll();
        return vehicles.stream()
                .map(VehicleRes::new)
                .collect(Collectors.toList());
    }

    // API-042 | 차량 통계 조회 (관리자)
    public Map<String, Long> getVehicleStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total",    (long) vehicleMapper.countByStatus("APPROVED"));
        stats.put("pending",  (long) vehicleMapper.countByStatus("PENDING"));
        stats.put("approved", (long) vehicleMapper.countByStatus("APPROVED"));
        stats.put("rejected", (long) vehicleMapper.countByStatus("REJECTED"));
        return stats;
    }

    // API-042 | 차량 승인 (관리자)
    public void approveVehicle(Long vehicleId, Long adminId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        vehicle.setVehicleId(vehicleId);
        vehicle.setApprovedBy(adminId);
        vehicleMapper.approveVehicle(vehicle);
    }

    // API-042 | 차량 거부 (관리자)
    public void rejectVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        vehicleMapper.rejectVehicle(vehicleId);
    }

    // API-043 | 내 차량 입출차 기록 조회
//    public List<ParkingLog> getMyVehicleLogs(Long userId) {
//        return parkingMapper.findLogsByUserId(userId);
//    }
}