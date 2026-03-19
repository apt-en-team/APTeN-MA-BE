package com.apt.facility.service;

import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import com.apt.facility.model.FacilityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
