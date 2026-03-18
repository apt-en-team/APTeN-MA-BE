package com.apt.facility.service;

import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityMapper facilityMapper;

    public List<Facility> getFacilities() {
        return facilityMapper.findAllActive();
    }
}
