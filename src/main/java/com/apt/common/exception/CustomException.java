package com.apt.common.exception;

import lombok.Getter;

// 프로젝트 전체에서 비즈니스 로직 예외를 던질 때 사용하는 커스텀 예외 클래스
// 사용 예시: throw new CustomException(ErrorCode.HOUSEHOLD_NOT_FOUND);
// GlobalExceptionHandler 에서 이 예외를 잡아 ErrorCode 기반으로 응답 반환
@Getter
public class CustomException extends RuntimeException {

    // 어떤 에러인지 나타내는 코드 (HTTP 상태코드 + 메시지 포함)
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}