package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class GxApprovalRes {
    private Long programId;
    private int confirmedCount; // 승인된 인원
    private int cancelledCount; // 정원 초과로 취소된 인원
}
