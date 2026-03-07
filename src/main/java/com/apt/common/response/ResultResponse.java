package com.apt.common.response;

import lombok.Getter;
import lombok.AllArgsConstructor;

// 모든 API 응답에 사용하는 공통 응답 포맷
// 사용 예시: ResultResponse.success("조회 성공", data)
//           ResultResponse.error("세대를 찾을 수 없습니다")
@Getter
@AllArgsConstructor  // 생성자: ResultResponse(String resultMessage, T resultData)
public class ResultResponse<T> {

    // 처리 결과 메시지 (예: "로그인 성공", "회원가입 실패")
    private String resultMessage;

    // 실제 응답 데이터 (제네릭 타입으로 어떤 데이터든 담을 수 있음, 실패 시 null)
    private T resultData;

    // 성공 응답 생성 헬퍼 메서드
    // 사용 예시: ResultResponse.success("조회 성공", data)
    public static <T> ResultResponse<T> success(String message, T data) {
        return new ResultResponse<>(message, data);
    }

    // 실패 응답 생성 헬퍼 메서드 (data = null)
    // 사용 예시: ResultResponse.error("세대를 찾을 수 없습니다")
    public static <T> ResultResponse<T> error(String message) {
        return new ResultResponse<>(message, null);
    }
}