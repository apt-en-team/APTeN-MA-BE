package com.apt.facility.controller;

import com.apt.common.response.ResultResponse;
import com.apt.facility.dto.request.FacilityReq;
import com.apt.facility.dto.request.FacilityTypeReq;
import com.apt.facility.dto.response.FacilityRes;
import com.apt.facility.dto.response.FacilityTypeRes;
import com.apt.facility.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 시설 관리 Controller */
@RestController
@RequiredArgsConstructor
public class FacilityController {

}
