package com.example.umc.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("USER4004", "사용자를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}