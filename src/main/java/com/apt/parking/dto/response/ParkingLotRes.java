package com.apt.parking.dto.response;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ParkingLotRes {
    private Long lotId;
    private String name;
    private int totalSpaces;
}