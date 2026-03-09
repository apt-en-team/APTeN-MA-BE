package com.apt.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 내 정보 수정 요청 DTO (PUT /api/users/me)
@Getter
@Setter
public class UpdateUserReq {

    // 수정할 이름
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    // 수정할 전화번호
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
}