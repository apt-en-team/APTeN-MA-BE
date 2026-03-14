package com.apt.reservation.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.reservation.dto.request.ReservationGetReq;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    //예약 가능 시간대 조회 (API-053 / UI-036)
    @GetMapping("/available")
    public ResultResponse<?> findAvailableTime(@ModelAttribute ReservationReq req){
        return ResultResponse.success("조회 성공", reservationService.findAvailableTime(req));
    }

    //예약 생성 (API-054 / UI-037)
    @PostMapping
    public ResultResponse<?> createReservation(@RequestBody ReservationReq req
                             , @AuthenticationPrincipal UserPrincipal userPrincipal){
        req.setUserId( userPrincipal.getUserId());
        System.out.println("facilityId = " + req.getFacilityId());
        return ResultResponse.success("등록 성공", reservationService.createReservation(req));
    }

    //내 예약 목록 (API-055 / UI-038) 상태기준 이용내역 or or'≥≥≥≥≥≥≥≥≥≥≥지난내역
    @GetMapping("/my")
    public ResultResponse<?> getReservation(ReservationGetReq req
                          , @AuthenticationPrincipal UserPrincipal userPrincipal){
        req.setUserId(userPrincipal.getUserId());
        System.out.println("userId = " + req.getUserId());
        return ResultResponse.success("조회 성공", reservationService.findReservation(req));
    }

    //예약 상세 조회 (API-056 / UI-039)
    @GetMapping("/{id}")
    public ResultResponse<?> getDetail(@PathVariable long id
                          , @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResultResponse.success("조회 성공", reservationService.getReservationDetail(id,userPrincipal.getUserId()));
    }


}
