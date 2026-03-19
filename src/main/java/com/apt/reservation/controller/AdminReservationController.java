package com.apt.reservation.controller;


import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.reservation.dto.request.GxUserReq;
import com.apt.reservation.dto.request.ReservationCalendarReq;
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

    @GetMapping("/count")
    public ResultResponse<?> getCount(@ModelAttribute ReservationGetReq req) {
        return ResultResponse.success("조회 성공", reservationService.getAdminReservationPageInfo(req));
    }

    //관리자 예약 상세 (API-059 / UI-041)
    @GetMapping("/{id:[0-9]+}")
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
        reservationService.forceAllCancel(facilityId);
        return ResultResponse.success("취소 성공", null);
    }

    //GX 승인처리 (API-061 / UI-043)
    @PatchMapping("/approve/{programId}")
    public ResultResponse<?> approveGx(@PathVariable Long programId) {
        return ResultResponse.success("승인 완료", reservationService.approveGx(programId));
    }

    //GX 대기 건수
    @GetMapping("/gx-pending-count")
    public ResultResponse<?> pendingGx() {
        return ResultResponse.success("GX 승인 대기중인 건수", reservationService.pendingGx());
    }

    //관리자 통계 조회
    @GetMapping("/today-stats")
    public ResultResponse<?> TodayStats() {
        return ResultResponse.success("조회 성공", reservationService.TodayStats());
    }

    //관리자 시설예약 현황 리트스
    @GetMapping("/facilitylist")
    public ResultResponse<?> getFacilityList() {
        return ResultResponse.success("조회 성공", reservationService.getFacilityList());
    }

    //관리자 캘린더페이지 조회
    @GetMapping("/facility")
    public ResultResponse<?> getReservationsByFacility(@ModelAttribute ReservationCalendarReq req) {
        return ResultResponse.success("조회 성공", reservationService.getReservationsByFacility(req));
    }

    //관리자 캘린더페이지 GX조회
    @GetMapping("/gx-programs")
    public ResultResponse<?> getReservationsByGxPrograms(@ModelAttribute ReservationCalendarReq req) {
        return ResultResponse.success("gx조회 성공", reservationService.getReservationsByGxPrograms(req));
    }

    //독서실 이용현황
    @GetMapping("/study-room")
    public ResultResponse<?> getStudyRoomDetail(@ModelAttribute ReservationCalendarReq req) {
        return ResultResponse.success("독서실 조회 성공", reservationService.getStudyRoomDetail(req));
    }

    //헬스장 이용현황
    @GetMapping("/gym")
    public ResultResponse<?> getGymDetail(@ModelAttribute ReservationCalendarReq req) {
        return ResultResponse.success("헬스장 조회 성공", reservationService.getGymDetail(req));
    }

    // 골프연습장 이용현황
    @GetMapping("/golf")
    public ResultResponse<?> getGolfDetail(@ModelAttribute ReservationCalendarReq req) {
        return ResultResponse.success("골프 조회 성공", reservationService.getGolfDetail(req));
    }

    // GX 프로그램별 사용자 목록 조회
    @GetMapping("/gx-users")
    public ResultResponse<?> getGxUsersByProgram(@ModelAttribute GxUserReq req) {
        return ResultResponse.success("GX 사용자 목록 조회 성공", reservationService.getGxUsersByProgram(req));
    }

    // 대시보드 오늘 시설 예약 현황
    @GetMapping("/dashboard-facility-summary")
    public ResultResponse<?> getDashboardFacilitySummary() {
        return ResultResponse.success("대시보드 시설 예약 현황 조회 성공",
                reservationService.getDashboardFacilitySummary());
    }

}
