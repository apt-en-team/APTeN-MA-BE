package com.apt.reservation.mapper;

import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.AvailableSlotRes;
import com.apt.reservation.dto.response.ReservationRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {

    //예약 가능 시간대 조회
    List<AvailableSlotRes> findAvailableTime(ReservationReq req);

    //예약 생성
    //좌석 중복 체크
    int countSeatReserved(ReservationReq req);
    //정원 초과 체크
    int countReserved(ReservationReq req);
    //예약 생성
    void insertReservation(ReservationReq req);
    ReservationRes findReservationById(Long reservationId);

    //Reservation findById(Long reservationId);

}
