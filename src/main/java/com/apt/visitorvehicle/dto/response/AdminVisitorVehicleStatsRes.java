package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminVisitorVehicleStatsRes {
    private int todayCount;
    private int tomorrowCount;
    private int monthCount;
    private int totalCount;
}