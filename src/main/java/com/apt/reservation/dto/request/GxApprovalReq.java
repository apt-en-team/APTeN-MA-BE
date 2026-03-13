package com.apt.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class GxApprovalReq {

    @NotNull
    private Long facilityId;

    @NotNull
    private LocalDate reservationDate;

    @NotNull
    private LocalTime startTime;

}
