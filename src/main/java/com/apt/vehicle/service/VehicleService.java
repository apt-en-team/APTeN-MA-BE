package com.apt.vehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.vehicle.dto.request.VehicleAdminSearchReq;
import com.apt.vehicle.dto.request.VehicleReq;
import com.apt.vehicle.dto.request.VehicleUpdateReq;
import com.apt.vehicle.dto.response.VehicleAdminRes;
import com.apt.vehicle.dto.response.VehicleLogRes;
import com.apt.vehicle.dto.response.VehiclePageRes;
import com.apt.vehicle.dto.response.VehicleRes;
import com.apt.vehicle.mapper.VehicleMapper;
import com.apt.vehicle.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 차량 서비스 | 차량 등록/수정/삭제/조회 비즈니스 로직 */
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleMapper vehicleMapper;

    /** API-038 | 내 차량 목록 조회 */
    public List<VehicleRes> getMyVehicles(Long userId) {
        return vehicleMapper.findByUserId(userId).stream()
                .map(VehicleRes::ofList)
                .toList();
    }

    /** API-039 | 차량 등록 (세대당 2대 제한, 번호판 중복 체크) */
    public VehicleRes registerVehicle(VehicleReq req, Long userId) {
        Long householdId = vehicleMapper.findHouseholdIdByUserId(userId);

        if (vehicleMapper.countByHouseholdId(householdId) >= 2) {
            throw new CustomException(ErrorCode.VEHICLE_LIMIT_EXCEEDED);
        }
        if (vehicleMapper.existsByLicensePlate(req.getLicensePlate()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setHouseholdId(householdId);
        vehicle.setLicensePlate(req.getLicensePlate());
        vehicle.setCarModel(req.getCarModel());
        vehicle.setCarType(req.getCarType());
        vehicle.setStatus("PENDING");

        vehicleMapper.insertVehicle(vehicle);
        return VehicleRes.ofRegister(vehicle);
    }

    /** API-040 | 차량 수정 */
    public VehicleRes updateVehicle(Long vehicleId, VehicleUpdateReq req, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        if (!vehicle.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        // 번호판 중복 체크 (자기 자신 제외)
        String cleanPlate   = req.getLicensePlate().replaceAll("\\s", "");;
        String currentPlate = vehicle.getLicensePlate().replaceAll("\\s", "");
        if (!cleanPlate.equals(currentPlate)) {
            if (vehicleMapper.existsByLicensePlate(req.getLicensePlate()) > 0) {
                throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
            }
        }

        vehicle.setLicensePlate(req.getLicensePlate());  // ← 추가
        vehicle.setCarModel(req.getCarModel());
        vehicleMapper.updateVehicle(vehicle);
        return VehicleRes.ofUpdate(vehicle);
    }

    /** API-041 | 차량 삭제 (본인 차량 체크) */
    public void deleteVehicle(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        if (!vehicle.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        vehicleMapper.deleteVehicle(vehicleId);
    }

    /** API-042 | 전체 차량 목록 조회 (필터 + 페이징) */
    public VehiclePageRes<VehicleAdminRes> getAllVehicles(VehicleAdminSearchReq req) {
        int offset            = req.getPage() * req.getSize();
        List<Vehicle> list    = vehicleMapper.findAll(req, req.getSize(), offset); // ← 수정
        long total            = vehicleMapper.countAll(req);                       // ← 수정
        int totalPages        = (int) Math.ceil((double) total / req.getSize());

        List<VehicleAdminRes> content = list.stream()
                .map(VehicleAdminRes::of)
                .toList();

        return new VehiclePageRes<>(content, req.getPage(), totalPages);
    }

    /** ADMIN | 차량 통계 조회 */
    public Map<String, Long> getVehicleStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total",    vehicleMapper.countAll(new VehicleAdminSearchReq()));
        stats.put("pending",  (long) vehicleMapper.countByStatus("PENDING"));
        stats.put("approved", (long) vehicleMapper.countByStatus("APPROVED"));
        stats.put("rejected", (long) vehicleMapper.countByStatus("REJECTED"));
        return stats;
    }

    /** API-043 | 내 차량 입출차 기록 조회 */
    public List<VehicleLogRes> getMyVehicleLogs(Long userId) {
        if (vehicleMapper.findByUserId(userId).isEmpty()) {
            throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        return vehicleMapper.findLogsByUserId(userId).stream()
                .map(log -> (VehicleLogRes) log)
                .toList();
    }

    /** ADMIN | 차량 승인 */
    public void approveVehicle(Long vehicleId, Long adminId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);

        vehicle.setApprovedBy(adminId);
        vehicleMapper.approveVehicle(vehicle);
    }

    /** ADMIN | 차량 거부 */
    public void rejectVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);

        vehicleMapper.rejectVehicle(vehicleId);
    }

    /** ADMIN | 동 목록 조회 */
    public List<String> getDongs() {
        return vehicleMapper.findDistinctDongs();
    }
}