package com.example.umc.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("FEMALE","여성"),
    MALE("MALE", "남성");

    private final String code;
    private final String description;
}