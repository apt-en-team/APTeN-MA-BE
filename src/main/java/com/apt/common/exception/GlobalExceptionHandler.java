package com.apt.common.exception;

import com.apt.common.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전역 예외 처리 클래스
// @RestControllerAdvice → 모든 Controller 에서 발생하는 예외를 여기서 일괄 처리
// 예외 발생 시 ResultResponse.error("에러메시지") 형태로 응답 반환
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // CustomException 처리
    // throw new CustomException(ErrorCode.XXX) 로 던진 예외를 여기서 캐치
    // ErrorCode 에 정의된 HTTP 상태코드와 메시지로 응답 반환
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResultResponse<?>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ResultResponse.error(e.getErrorCode().getMessage()));
    }

    // @Valid 유효성 검사 실패 처리
    // DTO 필드에 @NotBlank, @NotNull 등 검증 실패 시 여기서 캐치
    // 첫 번째 에러 메시지만 꺼내서 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<?>> handleValidationException(
            MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("유효성 검사 실패");

        return ResponseEntity
                .badRequest()
                .body(ResultResponse.error(message));
    }

    // 그 외 모든 예외 처리 (예상치 못한 서버 오류)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<?>> handleException(Exception e) {
        log.error("서버 오류 발생", e);
        return ResponseEntity
                .status(500)
                .body(ResultResponse.error("서버 오류가 발생했습니다"));
    }
}