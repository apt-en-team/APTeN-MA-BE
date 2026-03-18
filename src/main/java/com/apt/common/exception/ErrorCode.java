package com.apt.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 프로젝트 전체에서 사용하는 에러 코드 모음
// CustomException 에 담아서 GlobalExceptionHandler 가 처리
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 인증 관련 ─────────────────────────────────────────────────
    UNAUTHORIZED(401, "인증이 필요합니다"),
    FORBIDDEN(403, "권한이 없습니다"),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다"),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),

    // 메일 관련
    MAIL_SEND_FAILED(500, "이메일 발송에 실패했습니다."),

    // ── 사용자 관련 ───────────────────────────────────────────────
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL(400, "이미 사용중인 이메일입니다"),
    INVALID_CREDENTIALS(401, "이메일 또는 비밀번호가 일치하지 않습니다"),

    // ── 세대 관련 ─────────────────────────────────────────────────
    HOUSEHOLD_NOT_FOUND(404, "세대를 찾을 수 없습니다"),
    DUPLICATE_HOUSEHOLD(400, "이미 존재하는 세대입니다"),
    // 소속 회원이 있는 세대는 삭제 불가 (FR-010)
    HOUSEHOLD_HAS_MEMBER(400, "소속 회원이 있어 삭제할 수 없습니다"),
    HOUSEHOLD_ALREADY_LINKED(400, "이미 세대가 연동되어 있습니다"),

    // ── 게시판 관련 ───────────────────────────────────────────────
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다"),

    // ── 방문차량 관련 ─────────────────────────────────────────────
    VISITOR_VEHICLE_NOT_FOUND(404, "방문차량을 찾을 수 없습니다"),
    // 방문 예정일로 과거 날짜 선택 불가
    PAST_VISIT_DATE(400, "과거 날짜는 선택할 수 없습니다"),
    VISITOR_VEHICLE_NOT_APPROVED(400, "승인된 차량만 등록 취소할 수 있습니다"),
    // 고정 방문 차량 날짜 선택
    INVALID_DATE_RANGE(400,"종료일이 시작일보다 앞설 수 없습니다"),

    // ── 차량 관련 ─────────────────────────────────────────────────
    VEHICLE_NOT_FOUND(404, "차량을 찾을 수 없습니다"),
    // 세대당 최대 2대 제한 (FR-033, NFR-008)
    VEHICLE_LIMIT_EXCEEDED(400, "세대당 최대 2대까지 등록 가능합니다"),
    DUPLICATE_LICENSE_PLATE(400, "이미 등록된 차량번호입니다"),

    // ── 시설 관련 ─────────────────────────────────────────────────
    FACILITY_NOT_FOUND(404, "시설을 찾을 수 없습니다"),
    FACILITY_TYPE_NOT_FOUND(404, "시설 타입을 찾을 수 없습니다"),
    // 해당 타입으로 등록된 시설이 있으면 타입 삭제 불가 (FR-039)
    FACILITY_TYPE_HAS_FACILITY(400, "해당 타입의 시설이 존재하여 삭제할 수 없습니다"),
    // 예약이 존재하는 시설은 삭제 불가 (FR-042)
    FACILITY_HAS_RESERVATION(400, "예약이 있는 시설은 삭제할 수 없습니다"),

    // ── 예약 관련 ─────────────────────────────────────────────────
    // 동일 시설+날짜+시간+좌석 중복 예약 방지 (NFR-007)
    DUPLICATE_RESERVATION(409, "이미 예약된 시간입니다"),
    INVALID_DATE(409, "예약할 수 없는 날짜입니다"),
    // 헬스장/GX: max_capacity 초과 방지 (FR-044)
    RESERVATION_FULL(400, "정원이 초과되었습니다"),
    RESERVATION_NOT_FOUND(404, "예약을 찾을 수 없습니다"),
    // 예약 1시간 전까지만 취소 가능 (FR-047)
    CANCEL_TIME_EXPIRED(400, "예약 1시간 전까지만 취소 가능합니다"),
    // GX 예약 승인이 이미 처리된 경우
    GX_ALREADY_PROCESSED(400, "이미 처리된 강의입니다"),
    // [추가] 취소(삭제)할 예약이 존재하지 않는 경우
    NOT_FOUND_TO_CANCEL(404, "취소할 예약 정보를 찾을 수 없습니다"),

    // ── 서버 오류 ─────────────────────────────────────────────────
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다");

    // HTTP 상태 코드
    private final int status;

    // 사용자에게 보여줄 에러 메시지
    private final String message;
}
