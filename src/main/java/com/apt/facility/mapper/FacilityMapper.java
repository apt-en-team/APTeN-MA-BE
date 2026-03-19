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

    // 관리자 예약현황 페이지 시설 리스트
    List<FacilityListRes> getFacilityList();

}
