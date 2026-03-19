package com.apt.facility.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityListRes {

    private Long facilityId;
    private String facilityName;
    private Long typeId;
    private String typeName;
    private Long maxCapacity;

}
