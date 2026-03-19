package com.apt.reservation.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.facility.dto.response.FacilityListRes;
import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import com.apt.household.dto.response.PageRes;
import com.apt.reservation.dto.request.GxUserReq;
import com.apt.reservation.dto.request.ReservationCalendarReq;
import com.apt.reservation.dto.request.ReservationGetReq;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.*;
import com.apt.reservation.mapper.ReservationMapper;
import com.apt.reservation.model.GxProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final FacilityMapper facilityMapper;


    // 시설 조회 (없으면 404)
    private Facility getFacility(Long facilityId) {
        Facility facility = facilityMapper.findById(facilityId);
        if (facility == null) {
            throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);
        }
        return facility;
    }

    // 예약 상태 조회 (없으면 404)
    private ReservationRes getReservation(Long id) {
        ReservationRes reservation = reservationMapper.findReservationById(id);

        if (reservation == null) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        if ("CANCELLED".equals(reservation.getStatus()) || "COMPLETED".equals(reservation.getStatus())) {
            throw new CustomException(ErrorCode.NOT_FOUND_TO_CANCEL);
        }

        return reservation;
    }

    //매일 1시간마다 확인된 예약 완료로 변경
    @Scheduled(cron = "0 0 * * * *") // 매시간 정각
    @Transactional
    public void updateReservationStatusToCompleted() {

        System.out.println("예약 상태 업데이트 실행");

        // 오늘 날짜 이전(어제까지)이면서 상태가 'CONFIRMED'인 예약을 찾아서 'COMPLETED'로 변경
        // 오늘 날짜 이전(어제까지)이면서 상태가 'PENDING'인 예약을 찾아서 'CANCELLED'로 변경
        int updatedCount = reservationMapper.updateStatusToCompleted(LocalDate.now(), LocalTime.now());

        System.out.println("업데이트 개수: " + updatedCount);
    }

    //예약 가능 시간대 조회
    public List<AvailableSlotRes> findAvailableTime(ReservationReq req) {

        Facility facility = getFacility(req.getFacilityId());

        // 예약 건수 → Map으로 변환
        List<AvailableSlotRes> reserved = reservationMapper.findAvailableTime(req);
        Map<LocalTime, Integer> reservedMap = reserved.stream()
                .collect(Collectors.toMap(
                        AvailableSlotRes::getStartTime,
                        AvailableSlotRes::getReservedCount
                ));

        // 시간대 슬롯 생성
        List<AvailableSlotRes> result = new ArrayList<>();
        LocalTime current  = facility.getOpenTime();
        LocalTime closeTime = facility.getCloseTime();
        int slotMinutes = facility.getSlotDuration();

        while (current.isBefore(closeTime)) {
            LocalTime next = current.plusMinutes(slotMinutes);
            int reservedCount = reservedMap.getOrDefault(current, 0);

            AvailableSlotRes slot = new AvailableSlotRes();
            slot.setStartTime(current);
            slot.setEndTime(next);
            slot.setTotalCapacity(facility.getMaxCapacity());
            slot.setReservedCount(reservedCount);
            slot.setAvailableCount(facility.getMaxCapacity() - reservedCount);

            result.add(slot);
            current = next;
        }

        return result;
    }

    //예약 생성
    @Transactional
    public ReservationRes createReservation(ReservationReq req){
        Facility facility = getFacility(req.getFacilityId());
        //과거 날짜 체크
        if (req.getReservationDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_DATE);
        } //좌석 중복 체크 (seat_no != null인 경우)
        if(req.getSeatNo() != null) {
            int seatReserved = reservationMapper.countSeatReserved(req);
            if(seatReserved > 0 ){
                throw new CustomException(ErrorCode.DUPLICATE_RESERVATION);
            }
        } else { //정원 초과 체크 (seat_no == null인 경우)
            int reservedCount = reservationMapper.countReserved(req);
            if(reservedCount >= facility.getMaxCapacity() ){
                throw new CustomException(ErrorCode.RESERVATION_FULL);
            }
        }
        req.setStatus(req.getProgramId() != null ? "PENDING" : "CONFIRMED");

        reservationMapper.insertReservation(req);
        System.out.println("facilityId = " + req.getFacilityId());
        return reservationMapper.findReservationById(req.getReservationId());
    }

    //내 예약 목록 조회
    public List<ReservationRes> findReservation(ReservationGetReq req){
        return reservationMapper.findAll(req);
    }

    //예약 상세 조회
    public ReservationRes getReservationDetail(long id, long userId){

        ReservationRes reservation = reservationMapper.findReservationById(id);

        if (reservation == null) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        // 본인 예약 맞는지 확인
        if (!reservation.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return reservation;
    }

    //예약 취소(입주민)
    @Transactional
    public void cancelReservation(long id, long userId){

        //본인 예약 확인
        ReservationRes reservation = getReservationDetail(id, userId);
        //상태 확인
        reservation = getReservation(id);

        //1시간 전 예약 취소 방지
        if(reservation.getReservationDate().equals(LocalDate.now()) &&
                reservation.getStartTime().isBefore(LocalTime.now().plusHours(1)) || reservation.getReservationDate().isBefore(LocalDate.now()) ){
            throw new CustomException(ErrorCode.CANCEL_TIME_EXPIRED);
        }

        reservationMapper.cancelReservation(id);
    }

    //관리자 전체 예약 조회
    public List<ReservationListRes> getAdminReservationList(ReservationGetReq req) {
        return reservationMapper.findAllByAdmin(req);
    }

    // 페이지 정보 별도 반환
    public PageRes getAdminReservationPageInfo(ReservationGetReq req) {
        int totalCount = reservationMapper.countAllByAdmin(req);
        int maxPage    = (int) Math.ceil((double) totalCount / req.getSize());
        return new PageRes(maxPage, totalCount);  // HouseholdMapper에서 쓰던 PageRes 재사용
    }

    //관리자 예약 상세
    public ReservationRes getAdminReservationDetail(long id){
        return reservationMapper.findReservationById(id);
    }

    //예약 강제 취소 - 부분 (관리자)
    @Transactional
    public void forceCancel(long reservationId){
        //상태 확인
        ReservationRes reservation = getReservation(reservationId);

        //이후 날짜 확인
        if(reservation.getReservationDate().isBefore(LocalDate.now()) ){
            throw new CustomException(ErrorCode.CANCEL_TIME_EXPIRED);
        }
        reservationMapper.cancelReservation(reservationId);
    }

    //예약 강제 취소 - 전체 (관리자)
    @Transactional
    public void forceAllCancel(long facilityId){

        getFacility(facilityId);
        reservationMapper.cancelAllReservation(facilityId);
    }

    //GX 승인
    @Transactional
    public GxApprovalRes approveGx(Long programId) {

        // GX 프로그램 조회
        GxProgram gxProgram = reservationMapper.findProgramById(programId);
        if (gxProgram == null) {
            throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);
        }

        // 시설 존재 확인
        getFacility(gxProgram.getFacilityId());

        // PENDING 목록 (선착순)
        List<ReservationRes> list = reservationMapper.findPendingByProgramId(programId);
        if (list == null || list.isEmpty()) {
            throw new CustomException(ErrorCode.NO_PENDING_RESERVATION);
        }

        // 현재 이미 확정된 인원 수
        int currentConfirmedCount = reservationMapper.countConfirmedByProgramId(programId);

        // 남은 정원
        int remainCapacity = gxProgram.getMaxCapacity() - currentConfirmedCount;
        if (remainCapacity < 0) {
            remainCapacity = 0;
        }

        int confirmedCount = 0;
        int cancelledCount = 0;

        // 남은 정원만큼만 승인, 나머지는 취소
        for (ReservationRes gxReservation : list) {
            if (confirmedCount < remainCapacity) {
                reservationMapper.approveReservation(gxReservation.getReservationId());
                confirmedCount++;
            } else {
                reservationMapper.cancelOverflowReservation(gxReservation.getReservationId());
                cancelledCount++;
            }
        }

        // 최종 확정 인원이 정원에 도달하면 프로그램 마감
        if (currentConfirmedCount + confirmedCount >= gxProgram.getMaxCapacity()) {
            reservationMapper.closeProgram(programId);
        }

        GxApprovalRes res = new GxApprovalRes();
        res.setProgramId(programId);
        res.setConfirmedCount(confirmedCount);
        res.setCancelledCount(cancelledCount);

        return res;
    }

    //gx대기 카운트
    public GxPendingCount pendingGx(){
        return reservationMapper.pendingGx();
    }
    //관리자 통계카드
    public TodayStatsRes TodayStats(){
        return reservationMapper.TodayStats();
    }

    //관리자 예약현황 리스트
    public List<FacilityListRes> getFacilityList(){
        return facilityMapper.getFacilityList();
    }

    //관리자 캘린더페이지 조회
    public List<ReservationRes> getReservationsByFacility(ReservationCalendarReq req){
        return reservationMapper.getReservationsByFacility(req);
    }

    //관리자 캘린더페이지 GX조회
    public List<GxTotalCountListRes> getReservationsByGxPrograms(ReservationCalendarReq req){
        return reservationMapper.getReservationsByGxPrograms(req);
    }

    //독서실 남.여 나누기
    public StudyRoomDetailRes getStudyRoomDetail(ReservationCalendarReq req) {

        List<FacilityStatusRes> list = reservationMapper.getFacilityReservationRows(req);

        // 결과 객체
        StudyRoomDetailRes res = new StudyRoomDetailRes();

        // 남/여 리스트
        List<FacilityStatusRes> maleSeats = new ArrayList<>();
        List<FacilityStatusRes> femaleSeats = new ArrayList<>();

        for (FacilityStatusRes seat : list) {

            // 예약 없으면 EMPTY 처리
            if (seat.getStatus() == null) {
                seat.setStatus("EMPTY");
            }

            //시설id로 분리
            if (seat.getFacilityId().equals(1L)) {
                maleSeats.add(seat);
            } else if (seat.getFacilityId().equals(2L)) {
                femaleSeats.add(seat);
            }
        }

        res.setMaleSeats(maleSeats);
        res.setFemaleSeats(femaleSeats);

        return res;
    }

    //헬스장 예약 리스트
    public GymDetailRes getGymDetail(ReservationCalendarReq req){

        List<FacilityStatusRes> list = reservationMapper.getFacilityReservationRows(req);
        GymDetailRes res = new GymDetailRes();
        int totalCount = 0;
        int confirmedCount = 0;
        int cancelledCount = 0;

        for (FacilityStatusRes item : list) {
            if("CONFIRMED".equals(item.getStatus())){
                confirmedCount++;
            } else if ("CANCELLED".equals(item.getStatus())) {
                cancelledCount++;
            }
            totalCount++;
        }

        res.setTotalCount(totalCount);
        res.setConfirmedCount(confirmedCount);
        res.setCancelledCount(cancelledCount);
        res.setUserList(list);

        return res;
    }

    public List<FacilityStatusRes> getGolfDetail(ReservationCalendarReq req) {
        return reservationMapper.getFacilityReservationRows(req);
    }

    // GX 프로그램별 사용자 목록 조회
    public List<GxUserRes> getGxUsersByProgram(GxUserReq req) {
        return reservationMapper.getGxUsersByProgram(req);
    }

    // 대시보드 오늘 시설 예약 현황
    public List<DashboardFacilitySummaryRes> getDashboardFacilitySummary() {
        List<DashboardFacilitySummaryRes> list = reservationMapper.getDashboardFacilitySummary();

        for (DashboardFacilitySummaryRes item : list) {
            if (item.getTotalCount() == 0) {
                item.setPercent(0);
            } else {
                item.setPercent((int) Math.round((double) item.getCurrentCount() * 100 / item.getTotalCount()));
            }
        }

        return list;
    }

}
