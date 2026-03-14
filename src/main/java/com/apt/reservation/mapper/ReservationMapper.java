package com.apt.reservation.mapper;

import com.apt.reservation.dto.request.ReservationGetReq;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.AvailableSlotRes;
import com.apt.reservation.dto.response.ReservationRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {

    //스케쥴드 사용 자동 업데이트
    int updateStatusToCompleted(@Param("today") LocalDate today);

    //예약 가능 시간대 조회
    List<AvailableSlotRes> findAvailableTime(ReservationReq req);

    //예약 생성
    int countSeatReserved(ReservationReq req); //좌석 중복 체크
    int countReserved(ReservationReq req); //정원 초과 체크
    void insertReservation(ReservationReq req);//insert
    ReservationRes findReservationById(Long reservationId);//확인값 반환

    //내 예약목록 조회
    List<ReservationRes> findAll(ReservationGetReq req);

}
