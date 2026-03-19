package com.apt.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GxUserReq {
    private Long programId;
    private LocalDate startDate;
    private LocalDate endDate;
}
