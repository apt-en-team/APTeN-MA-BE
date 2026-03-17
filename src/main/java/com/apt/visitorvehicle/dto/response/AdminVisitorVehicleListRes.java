package com.apt.visitorvehicle.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AdminVisitorVehicleListRes {
    private List<AdminVisitorVehicleRes> content;
    private int page;
    private int totalPages;
    private int totalCount;
}