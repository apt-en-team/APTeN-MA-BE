package com.apt.reservation.mapper;

import com.apt.reservation.dto.request.GxUserReq;
import com.apt.reservation.dto.request.ReservationCalendarReq;
import com.apt.reservation.dto.request.ReservationGetReq;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.*;
import com.apt.reservation.model.GxProgram;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ReservationMapper {

    //스케쥴드 사용 자동 업데이트
    int updateStatusToCompleted(@Param("today") LocalDate today, @Param("now") LocalTime now);

    //예약 가능 시간대 조회
    List<AvailableSlotRes> findAvailableTime(ReservationReq req);

    //예약 생성
    int countSeatReserved(ReservationReq req); //좌석 중복 체크
    int countReserved(ReservationReq req); //정원 초과 체크
    void insertReservation(ReservationReq req);//insert

    //예약 상세
    ReservationRes findReservationById(Long reservationId);//확인값 반환

    //내 예약목록 조회
    List<ReservationRes> findAll(ReservationGetReq req);

    //예약 취소
    int cancelReservation(long id);

    //관리자 예약목록 조회
    List<ReservationListRes> findAllByAdmin(ReservationGetReq req);
    //페이지 조회
    int countAllByAdmin(ReservationGetReq req);

    //예약 전체 취소(관리자)
    int cancelAllReservation(long facilityId);

    //Gx프로그램 조회
    GxProgram findProgramById(Long programId);
    //대기목록 조회(선착순 정렬)
    List<ReservationRes> findPendingByProgramId(Long programId);
    //대기 -> 확정 처리
    int approveReservation(long reservationId);
    //대기 -> 취소 처리 (정원초과 분)
    int cancelOverflowReservation(long reservationId);
    //승인인원 = 맥스인원 이면 프로그램 CLOSED
    int closeProgram (long programId);

    GxPendingCount pendingGx();
    TodayStatsRes TodayStats();
    List<ReservationRes> getReservationsByFacility(ReservationCalendarReq req);
    List<GxTotalCountListRes> getReservationsByGxPrograms(ReservationCalendarReq req);

    // GX 현재 확정 인원 수
    int countConfirmedByProgramId(Long programId);

    //독서실,헬스장, 골프연습장 리스트
    List<FacilityStatusRes> getFacilityReservationRows(ReservationCalendarReq req);

    //gx 리스트
    List<GxUserRes> getGxUsersByProgram(GxUserReq req);

    // 대시보드 오늘 시설 예약 현황
    List<DashboardFacilitySummaryRes> getDashboardFacilitySummary();

}
