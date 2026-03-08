package com.apt.household.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResidentDto {
    private Long   userId;
    private String name;
    private String phone;
    private String email;
}
