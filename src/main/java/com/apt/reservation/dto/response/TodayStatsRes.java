package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayStatsRes {
    private int todayTotal;
    private int todayConfirmed;
    private int todayCancelled;
    private int todayPending;
    private int monthTotal;
    private int totalCount;
}
