package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GxTotalCountListRes {
    private Long facilityId;
    private Long programId;
    private Long maxCapacity;
    private String facilityName;
    public int pendingCount;
    public int confirmedCount;
    public int cancelledCount;
}
