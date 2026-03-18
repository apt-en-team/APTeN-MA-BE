package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationGetReq {
    private long userId;
    private String facilityName;
    private String status;
    private String userName;

    private Long facilityId;
    private Long typeId;

    private LocalDate reservationDate;
    private LocalDate startDate;
    private LocalDate endDate;

    private int page;
    private int size;
    private int startIdx;

    public void setPage(int page) {
        this.page = page;
        this.startIdx = (page - 1) * this.size;
    }

    public void setSize(int size) {
        this.size = size;
        this.startIdx = (this.page - 1) * size;
    }
}
