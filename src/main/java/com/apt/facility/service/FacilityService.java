package com.apt.facility.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.facility.dto.request.FacilityReq;
import com.apt.facility.dto.request.FacilityTypeReq;
import com.apt.facility.dto.response.FacilityRes;
import com.apt.facility.dto.response.FacilityTypeRes;

import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import com.apt.facility.model.FacilityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/** 시설 서비스 | 시설 및 시설 타입 CRUD 비즈니스 로직 */
@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityMapper facilityMapper;

    // 실제 운영 중인 시설 목록 조회
    public List<Facility> getFacilities() {
        return facilityMapper.findAllActiveFacilities();
    }

    // 시설 타입 목록 조회
    public List<FacilityType> getFacilityTypes() {
        return facilityMapper.findAllTypes();
    }

    // 시설 카테고리 목록 조회
    public List<FacilityType> getFacilityCategory() {
        return facilityMapper.getFacilityCategory();
    }

    /** API-044 | 시설 타입 전체 조회 */
    public List<FacilityTypeRes> getAllTypes() {
        return facilityMapper.findAllTypes().stream()
                .map(FacilityTypeRes::of)
                .toList();
    }

    /** API-045 | 시설 타입 등록 */
    public FacilityTypeRes createType(FacilityTypeReq req) {
        FacilityType type = new FacilityType();
        type.setName(req.getName());
        type.setDescription(req.getDescription());
        facilityMapper.insertType(type);
        return FacilityTypeRes.of(type);
    }

    /** API-046 | 시설 타입 수정 */
    public FacilityTypeRes updateType(Long typeId, FacilityTypeReq req) {
        FacilityType type = facilityMapper.findTypeById(typeId);
        if (type == null) throw new CustomException(ErrorCode.FACILITY_TYPE_NOT_FOUND);

        type.setName(req.getName());
        type.setDescription(req.getDescription());
        facilityMapper.updateType(type);
        return FacilityTypeRes.of(type);
    }

    /** API-047 | 시설 타입 삭제 (해당 타입 시설 존재 시 삭제 불가) */
    public void deleteType(Long typeId) {
        FacilityType type = facilityMapper.findTypeById(typeId);
        if (type == null) throw new CustomException(ErrorCode.FACILITY_TYPE_NOT_FOUND);

        if (facilityMapper.countFacilitiesByTypeId(typeId) > 0) {
            throw new CustomException(ErrorCode.FACILITY_TYPE_HAS_FACILITY);
        }
        facilityMapper.deleteType(typeId);
    }

    /** API-048 | 시설 목록 조회 (타입 필터) */
    public List<FacilityRes> getAllFacilities(Long typeId) {
        return facilityMapper.findAll(typeId).stream()
                .map(FacilityRes::of)
                .toList();
    }

    /** API-049 | 시설 상세 조회 */
    public FacilityRes getFacility(Long facilityId) {
        Facility facility = facilityMapper.findById(facilityId);
        if (facility == null) throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);
        return FacilityRes.of(facility);
    }

    /** API-050 | 시설 등록 */
    public FacilityRes createFacility(FacilityReq req) {
        // ✅ 필수값 검증
        if (req.getTypeId() == null)
            throw new CustomException(ErrorCode.FACILITY_TYPE_REQUIRED);
        if (req.getName() == null || req.getName().isBlank())
            throw new CustomException(ErrorCode.FACILITY_NAME_REQUIRED);
        if (req.getMaxCapacity() == null)
            throw new CustomException(ErrorCode.FACILITY_CAPACITY_REQUIRED);

        if (facilityMapper.findTypeById(req.getTypeId()) == null)
            throw new CustomException(ErrorCode.FACILITY_TYPE_NOT_FOUND);

        Facility facility = new Facility();
        facility.setTypeId(req.getTypeId());
        facility.setName(req.getName());
        facility.setDescription(req.getDescription());
        facility.setMaxCapacity(req.getMaxCapacity());
        facility.setOpenTime(LocalTime.parse(req.getOpenTime()));
        facility.setCloseTime(LocalTime.parse(req.getCloseTime()));
        facility.setSlotDuration(req.getSlotDuration());
        facility.setIsActive(req.isActive());

        facilityMapper.insertFacility(facility);
        return FacilityRes.of(facility);
    }

    /** API-051 | 시설 수정 */
    public FacilityRes updateFacility(Long facilityId, FacilityReq req) {
        // ✅ 필수값 검증
        if (req.getTypeId() == null)
            throw new CustomException(ErrorCode.FACILITY_TYPE_REQUIRED);
        if (req.getName() == null || req.getName().isBlank())
            throw new CustomException(ErrorCode.FACILITY_NAME_REQUIRED);
        if (req.getMaxCapacity() == null)
            throw new CustomException(ErrorCode.FACILITY_CAPACITY_REQUIRED);

        Facility facility = facilityMapper.findById(facilityId);
        if (facility == null) throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);

        facility.setTypeId(req.getTypeId());
        facility.setName(req.getName());
        facility.setDescription(req.getDescription());
        facility.setMaxCapacity(req.getMaxCapacity());
        facility.setOpenTime(LocalTime.parse(req.getOpenTime()));
        facility.setCloseTime(LocalTime.parse(req.getCloseTime()));
        facility.setSlotDuration(req.getSlotDuration());
        facility.setIsActive(req.isActive());
        facility.setPrice(req.getPrice());

        facilityMapper.updateFacility(facility);
        return FacilityRes.of(facility);
    }

    /** API-052 | 시설 삭제 (예약 존재 시 삭제 불가) */
    public void deleteFacility(Long facilityId) {
        Facility facility = facilityMapper.findById(facilityId);
        if (facility == null) throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);

        if (facilityMapper.countReservationsByFacilityId(facilityId) > 0) {
            throw new CustomException(ErrorCode.FACILITY_HAS_RESERVATION);
        }
        facilityMapper.deleteFacility(facilityId);
    }

}