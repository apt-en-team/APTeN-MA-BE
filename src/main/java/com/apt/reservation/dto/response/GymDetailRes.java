package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GymDetailRes {
    private int totalCount;
    private int confirmedCount;
    private int cancelledCount;
    private List<FacilityStatusRes> userList;
}
