package com.apt.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyRoomDetailRes {
    private List<FacilityStatusRes> maleSeats;
    private List<FacilityStatusRes> femaleSeats;
}
