package com.apt.parking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLot {
    private Long lotId;
    private String name;
    private int totalSpaces;
}