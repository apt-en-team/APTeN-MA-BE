package com.apt.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRes {
    private Long userId;
    private String name;
    private String phone;
    private String dong;
    private String ho;
    private String status; // PENDING / APPROVED / REJECTED
}