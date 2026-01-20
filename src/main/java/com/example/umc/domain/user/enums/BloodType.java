package com.example.umc.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BloodType {
    A("A", "A형"),
    B("B","B형"),
    O("O","O형"),
    AB("AB","AB형"),
    ETC("ETC","직접 입력하기");

    private final String code;
    private final String description;
}