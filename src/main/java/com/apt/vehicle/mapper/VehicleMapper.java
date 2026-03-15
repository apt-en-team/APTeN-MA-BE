package com.apt.vehicle.mapper;

import com.apt.vehicle.dto.request.VehicleAdminSearchReq;
import com.apt.vehicle.model.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehicleMapper {

    /** 전체 차량 목록 조회 (필터 + 페이징) | API-042 */
    List<Vehicle> findAll(VehicleAdminSearchReq req,
                          @Param("size")   int size,
                          @Param("offset") int offset);

    /** 전체 차량 수 조회 (필터) | API-042 페이징용 */
    long countAll(VehicleAdminSearchReq req);

    /** 동 목록 조회 (필터용) ← 추가 */
    List<String> findDistinctDongs();

    /** 내 차량 목록 조회 | API-038 */
    List<Vehicle> findByUserId(Long userId);

    /** 차량 단건 조회 | API-040, 041 */
    Vehicle findById(Long vehicleId);

    /** 세대 차량 수 조회 (2대 제한 체크) | API-039 */
    int countByHouseholdId(Long householdId);

    /** 번호판 중복 체크 | API-039 */
    int existsByLicensePlate(String licensePlate);

    /** 번호판으로 차량 ID 조회 */
    Long findIdByLicensePlate(String licensePlate);

    /** 차량 등록 | API-039 */
    void insertVehicle(Vehicle vehicle);

    /** 차량 수정 | API-040 */
    void updateVehicle(Vehicle vehicle);

    /** 차량 삭제 | API-041 */
    void deleteVehicle(Long vehicleId);

    /** 차량 승인 | ADMIN */
    void approveVehicle(Vehicle vehicle);

    /** 차량 거부 | ADMIN */
    void rejectVehicle(Long vehicleId);

    /** 상태별 차량 수 조회 | ADMIN */
    int countByStatus(String status);

    /** 내 차량 입출차 기록 조회 | API-043 */
    List<?> findLogsByUserId(Long userId);

    /** householdId 조회 | API-039 */
    Long findHouseholdIdByUserId(Long userId);
}