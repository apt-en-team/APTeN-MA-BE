package com.apt.reservation.mapper;

import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.AvailableSlotRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {

    List<AvailableSlotRes> findAvailableTime(ReservationReq req);

}
