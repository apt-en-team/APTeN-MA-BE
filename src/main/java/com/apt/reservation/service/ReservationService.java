package com.apt.reservation.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.facility.mapper.FacilityMapper;
import com.apt.facility.model.Facility;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.dto.response.AvailableSlotRes;
import com.apt.reservation.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<AvailableSlotRes> findAvailableTime(ReservationReq req) {

        // 시설 조회 (없으면 404)
        Facility facility = facilityMapper.findById(req.getFacilityId());
        if (facility == null) {
            throw new CustomException(ErrorCode.FACILITY_NOT_FOUND);
        }

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
        int slotMinutes    = facility.getSlotDuration();

        while (current.isBefore(closeTime)) {
            LocalTime next        = current.plusMinutes(slotMinutes);
            int reservedCount     = reservedMap.getOrDefault(current, 0);

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
}
