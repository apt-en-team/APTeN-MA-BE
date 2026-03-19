package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GxUserRes {
    private Long reservationId;
    private Long userId;
    private String userName;
    private String dong;
    private String ho;
    private String status;
    private LocalDateTime createdAt;
}
