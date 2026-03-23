package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingUserDto {
    private Long   userId;
    private String name;
    private String phone;
}
