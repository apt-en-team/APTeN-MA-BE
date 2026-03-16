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
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {
    private final ReservationService reservationService;

    //관리자 전체 예약 조회 (API-058 / UI-040)
    @GetMapping
    public ResultResponse<?> getList(@ModelAttribute ReservationGetReq req){
        return ResultResponse.success("조회 성공", reservationService.getAdminReservationList(req));
    }

    //관리자 예약 상세 (API-059 / UI-041)
    @GetMapping("/{id}")
    public ResultResponse<?> getDetail(@PathVariable long id){
        return ResultResponse.success("조회 성공", reservationService.getAdminReservationDetail(id));
    }

    //예약 강제 취소 - 부분 (API-060 / UI-041)
    @DeleteMapping("/{reservationId}")
    public ResultResponse<?> forceCancel(@PathVariable long reservationId){
        reservationService.forceCancel(reservationId);
        return ResultResponse.success("취소 성공", null);
    }

    //예약 강제 취소 - 전체 (API-060 / UI-041)
    @DeleteMapping("/facility/{facilityId}")
    public ResultResponse<?> forceAllCancel(@PathVariable long facilityId){
        reservationService.forceCancel(facilityId);
        return ResultResponse.success("취소 성공", null);
    }

    //GX 승인처리 (API-061 / UI-043)
    @PatchMapping("/approve/{programId}")
    public ResultResponse<?> approveGx(@PathVariable Long programId) {
        return ResultResponse.success("승인 완료", reservationService.approveGx(programId));
    }

}
