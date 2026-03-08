package com.apt.household.controller;

import com.apt.common.response.ResultResponse;
import com.apt.household.dto.request.HouseholdGetReq;
import com.apt.household.dto.request.HouseholdHistoryReq;
import com.apt.household.dto.request.HouseholdReq;
import com.apt.household.service.HouseholdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// 세대 관리 API 컨트롤러
// 기본 URL: /api/admin (모든 엔드포인트는 ADMIN 권한 필요)
// 관련 FR: FR-010 (세대 관리), FR-011 (이력 관리)
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class HouseholdController {

    // 세대 관련 비즈니스 로직
    private final HouseholdService householdService;

    // ── API-014: 세대 목록 조회 ───────────────────────────────────
    // GET /api/admin/households?page=0&size=10
    // 페이징 처리된 세대 목록 반환 (동/호/상태/차량수 포함)
    @GetMapping("/households")
    public ResultResponse<?> getHouseholds(HouseholdGetReq req) {
        return ResultResponse.success("조회 성공", householdService.getHouseholds(req));
    }

    @GetMapping("/households/maxPage")
    public ResultResponse<?> getMaxPage(HouseholdGetReq req) {
        return ResultResponse.success("최대 페이지 조회 성공", householdService.getMaxPage(req));
    }

    @GetMapping("/households/dongs")
    public ResultResponse<?> getAllDongs() {
        return ResultResponse.success("동 목록 조회 성공", householdService.getAllDongs());
    }

    @GetMapping("/households/{householdId}/pending")
    public ResultResponse<?> getPendingUsers(@PathVariable Long householdId) {
        return ResultResponse.success("대기 유저 조회 성공", householdService.getPendingUsers(householdId));
    }

    // ── 통계 조회 (프론트 상단 카드용) ───────────────────────────
    // GET /api/admin/households/stats
    // 전체/입주/공실/이번달 전입·전출 건수 반환
    @GetMapping("/households/stats")
    public ResultResponse<?> getStats() {
        return ResultResponse.success("통계 조회 성공", householdService.getStats());
    }

    // ── API-015: 세대 등록 ────────────────────────────────────────
    // POST /api/admin/households
    // Body: { dong, ho }
    // 동/호 중복 시 400 에러 반환
    @PostMapping("/households")
    public ResultResponse<?> createHousehold(@Valid @RequestBody HouseholdReq req) {
        return ResultResponse.success("세대 등록 성공", householdService.createHousehold(req));
    }

    // ── API-016: 세대 수정 ────────────────────────────────────────
    // PUT /api/admin/households/{id}
    // Body: { dong, ho }
    // 세대가 없으면 404 에러 반환
    @PutMapping("/households/{id}")
    public ResultResponse<?> updateHousehold(
            @PathVariable Long id,
            @Valid @RequestBody HouseholdReq req) {

        return ResultResponse.success("세대 수정 성공", householdService.updateHousehold(id, req));
    }

    // ── API-017: 세대 삭제 ────────────────────────────────────────
    // DELETE /api/admin/households/{id}
    // 소속 회원(is_deleted=0)이 있으면 400 에러 반환
    // 세대가 없으면 404 에러 반환
    @DeleteMapping("/households/{id}")
    public ResultResponse<?> deleteHousehold(@PathVariable Long id) {
        householdService.deleteHousehold(id);
        return ResultResponse.success("세대 삭제 성공", null);
    }

    // ── API-018: 입주/퇴거 이력 등록 ─────────────────────────────
    // POST /api/admin/households/{id}/history
    // Body: { userId, status("입주" | "퇴거") }
    // 이력은 삭제 없이 누적 관리 (FR-011)
    @PostMapping("/households/{id}/history")
    public ResultResponse<?> createHistory(
            @PathVariable Long id,
            @Valid @RequestBody HouseholdHistoryReq req) {

        return ResultResponse.success("이력 등록 성공", householdService.createHistory(id, req));
    }

    // ── API-019: 입주/퇴거 이력 조회 ─────────────────────────────
    // GET /api/admin/households/{id}/history
    // 해당 세대의 전체 이력을 최신순으로 반환 (입주민 이름 포함)
    @GetMapping("/households/{id}/history")
    public ResultResponse<?> getHistory(@PathVariable Long id) {
        return ResultResponse.success("이력 조회 성공", householdService.getHistory(id));
    }

    //모달 등록 입주민 조회
    @GetMapping("/households/{householdId}/residents")
    public ResultResponse<?> getResidents(@PathVariable Long householdId) {
        return ResultResponse.success("입주민 조회 성공", householdService.getResidents(householdId));
    }

}