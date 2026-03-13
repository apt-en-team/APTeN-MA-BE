package com.apt.reservation.controller;

import com.apt.common.response.ResultResponse;
import com.apt.reservation.dto.request.ReservationReq;
import com.apt.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/available")
    public ResultResponse<?> findAvailableTime(@ModelAttribute ReservationReq req){
        return ResultResponse.success("성공", reservationService.findAvailableTime(req));
    }

}
