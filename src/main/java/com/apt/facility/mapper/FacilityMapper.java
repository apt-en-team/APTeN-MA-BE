package com.apt.facility.mapper;

import com.apt.facility.model.Facility;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 시설 관련 MyBatis 매퍼 인터페이스
 * 팀원(손지혜) 담당 테이블: facility, facility_type
 * 예약 기능에서 필요한 조회 메서드만 미리 작성해둠
 */
@Mapper
public interface FacilityMapper {

    /**
     * 시설 단건 조회
     * - 예약 생성 시 시설 존재 여부 확인
     * - 예약 가능 시간대 조회 시 open_time, close_time, slot_duration, max_capacity 사용
     *
     * @param facilityId 시설 ID
     * @return Facility (없으면 null → 서비스에서 404 처리)
     */
    Facility findById(Long facilityId);

    /**
     * 전체 시설 목록 조회 (운영 중인 시설만)
     * - 예약 화면 시설 드롭다운용
     *
     * @return is_active = 1 인 시설 리스트
     */
    List<Facility> findAllActive();

}