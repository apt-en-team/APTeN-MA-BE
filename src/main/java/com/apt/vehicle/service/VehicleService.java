package com.apt.vehicle.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.vehicle.dto.request.VehicleAdminRegisterReq;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public VehicleRes registerVehicle(VehicleReq req, Long userId) {
        // 1. 유저 ID로 세대(household) 정보 조회
        Long householdId = vehicleMapper.findHouseholdIdByUserId(userId);
        if (householdId == null) {
            throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        // 2. 번호판 필수값 체크 (가장 먼저)
        if (req.getLicensePlate() == null || req.getLicensePlate().isBlank()) {
            throw new CustomException(ErrorCode.LICENSE_PLATE_REQUIRED);
        }

        // 3. 번호판 공백 제거
        String cleanLicensePlate = req.getLicensePlate().replaceAll("\\s", "");

        // 4. 차 모델 / 차량 종류 유효성 검사
        if (req.getCarModel() == null || req.getCarModel().isBlank()) {
            throw new CustomException(ErrorCode.VEHICLE_MODEL_REQUIRED);
        }
        if (req.getCarType() == null || req.getCarType().isBlank()) {
            throw new CustomException(ErrorCode.VEHICLE_TYPE_REQUIRED);
        }

        // 5. 세대당 차량 대수 제한 (2대)
        if (vehicleMapper.countByHouseholdId(householdId) >= 2) {
            throw new CustomException(ErrorCode.VEHICLE_LIMIT_EXCEEDED);
        }

        // 6. 번호판 중복 체크
        if (vehicleMapper.existsByLicensePlate(cleanLicensePlate) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
        }

        // 7. Entity 생성 및 저장
        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setHouseholdId(householdId);
        vehicle.setLicensePlate(cleanLicensePlate);
        vehicle.setCarModel(req.getCarModel());
        vehicle.setCarType(req.getCarType());
        vehicle.setStatus("PENDING");

        vehicleMapper.insertVehicle(vehicle);
        return VehicleRes.ofRegister(vehicle);
    }

    /** API-040 | 차량 수정 */
    @Transactional
    public VehicleRes updateVehicle(Long vehicleId, VehicleUpdateReq req, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        if (!vehicle.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        // 번호판 변경 시 공백 제거 + 중복 체크 (자기 자신 제외)
        if (req.getLicensePlate() != null && !req.getLicensePlate().isBlank()) {
            String cleanPlate = req.getLicensePlate().replaceAll("\\s", "");
            if (!cleanPlate.equals(vehicle.getLicensePlate())) {
                int count = vehicleMapper.existsByLicensePlate(cleanPlate);
                if (count > 0) throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
            }
            vehicle.setLicensePlate(cleanPlate);
        }

        // 차종 변경
        if (req.getCarModel() != null && !req.getCarModel().isBlank()) {
            vehicle.setCarModel(req.getCarModel());
        }

        vehicleMapper.updateVehicle(vehicle);
        return VehicleRes.ofUpdate(vehicle);
    }

    /** API-041 | 차량 삭제 (본인 차량 체크) */
    @Transactional
    public void deleteVehicle(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);
        if (!vehicle.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        vehicleMapper.deleteVehicle(vehicleId);
    }

    /** API-042 | 전체 차량 목록 조회 (필터 + 페이징) */
    public VehiclePageRes<VehicleAdminRes> getAllVehicles(VehicleAdminSearchReq req) {
        int offset            = req.getPage() * req.getSize();
        List<Vehicle> list    = vehicleMapper.findAll(req, req.getSize(), offset);
        long total            = vehicleMapper.countAll(req);
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
    @Transactional
    public void approveVehicle(Long vehicleId, Long adminId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);

        vehicle.setApprovedBy(adminId);
        vehicleMapper.approveVehicle(vehicle);
    }

    /** ADMIN | 차량 거부 */
    @Transactional
    public void rejectVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) throw new CustomException(ErrorCode.VEHICLE_NOT_FOUND);

        vehicleMapper.rejectVehicle(vehicleId);
    }

    /** 차량번호 중복 확인 */
    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleMapper.existsByLicensePlate(licensePlate) > 0;
    }

    /** ADMIN | 동 목록 조회 */
    public List<String> getDongs() {
        return vehicleMapper.findDistinctDongs();
    }

    /** ADMIN | 차량 등록 */
    @Transactional
    public VehicleRes adminRegisterVehicle(VehicleAdminRegisterReq req) {
        Long householdId = vehicleMapper.findHouseholdIdByUserId(req.getUserId());
        if (householdId == null) throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);

        // 1. 번호판 필수값 체크 (가장 먼저)
        if (req.getLicensePlate() == null || req.getLicensePlate().isBlank()) {
            throw new CustomException(ErrorCode.LICENSE_PLATE_REQUIRED);
        }

        // 2. 번호판 공백 제거
        String cleanLicensePlate = req.getLicensePlate().replaceAll("\\s", "");

        // 3. 나머지 유효성 체크
        if (vehicleMapper.countByHouseholdId(householdId) >= 2) {
            throw new CustomException(ErrorCode.VEHICLE_LIMIT_EXCEEDED);
        }
        if (vehicleMapper.existsByLicensePlate(cleanLicensePlate) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_LICENSE_PLATE);
        }
        if (req.getCarModel() == null || req.getCarModel().isBlank()) {
            throw new CustomException(ErrorCode.VEHICLE_MODEL_REQUIRED);
        }
        if (req.getCarType() == null || req.getCarType().isBlank()) {
            throw new CustomException(ErrorCode.VEHICLE_TYPE_REQUIRED);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(req.getUserId());
        vehicle.setHouseholdId(householdId);
        vehicle.setLicensePlate(cleanLicensePlate); // 공백 제거된 번호판 저장
        vehicle.setCarModel(req.getCarModel());
        vehicle.setCarType(req.getCarType());
        vehicle.setStatus(req.getStatus());

        vehicleMapper.insertVehicle(vehicle);
        return VehicleRes.ofRegister(vehicle);
    }
}