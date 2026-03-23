package com.apt.facility.mapper;

import com.apt.facility.dto.response.FacilityListRes;
import com.apt.facility.model.Facility;
import com.apt.facility.model.FacilityType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 시설 관련 MyBatis 매퍼 인터페이스
 * 팀원(손지혜) 담당 테이블: facility, facility_type
 * 예약 기능에서 필요한 조회 메서드만 미리 작성해둠
 */
@Mapper
public interface FacilityMapper {

    // 시설 단건 조회
    Facility findById(Long facilityId);

    // 운영 중인 실제 시설 목록
    List<Facility> findAllActiveFacilities();

    // 시설 타입 목록
    List<FacilityType> findAllTypes();

    // 시설 카테고리 목록
    List<FacilityType> getFacilityCategory();

    // 관리자 예약현황 페이지 시설 리스트
    List<FacilityListRes> getFacilityList();

    /** 시설 타입 단건 조회 */
    FacilityType findTypeById(Long typeId);

    /** 시설 타입 등록 | API-045 */
    void insertType(FacilityType facilityType);

    /** 시설 타입 수정 | API-046 */
    void updateType(FacilityType facilityType);

    /** 시설 타입 삭제 | API-047 */
    void deleteType(Long typeId);

    /** 해당 타입 시설 존재 여부 (삭제 시 체크) | API-047 */
    int countFacilitiesByTypeId(Long typeId);

    /** 시설 목록 조회 (타입 필터) | API-048 */
    List<Facility> findAll(Long typeId);

    /** 시설 등록 | API-050 */
    void insertFacility(Facility facility);

    /** 시설 수정 | API-051 */
    void updateFacility(Facility facility);

    /** 시설 삭제 | API-052 */
    void deleteFacility(Long facilityId);

    /** 해당 시설 예약 존재 여부 (삭제 시 체크) | API-052 */
    int countReservationsByFacilityId(Long facilityId);

    /** 오늘 전체 예약 건수 조회 */
    int countTodayReservations();

    /** 현재 시간 기준 이용 중인 인원 조회 */
    int countCurrentInUse();



}