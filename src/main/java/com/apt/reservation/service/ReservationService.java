package com.apt.reservation.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import com.apt.reservation.dto.request.ReservationGetReq;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.AvailableSlotRes;
import com.apt.reservation.dto.response.ReservationListRes;
import com.apt.reservation.dto.response.ReservationRes;
import com.apt.reservation.mapper.ReservationMapper;
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

    //매일 자정 확인된 예약 완료로 변경
    // 0 0 0 * * * 는 매일 00:00:00을 의미
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateReservationStatusToCompleted() {
        // 오늘 날짜 이전(어제까지)이면서 상태가 'CONFIRMED'인 예약을 찾아서 'COMPLETED'로 변경
        // 오늘 날짜 이전(어제까지)이면서 상태가 'PENDING'인 예약을 찾아서 'CANCELLED'로 변경
        int updatedCount = reservationMapper.updateStatusToCompleted(LocalDate.now());
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
        //본인 예약 맞는지 확인
        if(!reservation.getUserId().equals(userId)){
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return reservation;
    }

    //예약 취소(입주민)
    public void cancelReservation(long id, long userId){

        //본인 예약 확인
        ReservationRes reservation = getReservationDetail(id, userId);

        //1시간 전 예약 취소 방지
        if(reservation.getReservationDate().equals(LocalDate.now()) &&
                reservation.getStartTime().isBefore(LocalTime.now().plusHours(1))){
            throw new CustomException(ErrorCode.CANCEL_TIME_EXPIRED);
        }

        reservationMapper.cancelReservation(id);
    }

}
