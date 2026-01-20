package com.example.umc.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    KO("ko", "한국어"),
    EN("en", "영어 (English)"),
    JP("ja", "일본어 (日本語)"),
    CN("zh", "중국어 (中文)"),
    VI("vi", "베트남어 (Tiếng Việt)"); // 세미콜론(;) 확인!

    private final String code;        // "ko", "ja" 등
    private final String description; // "한국어", "일본어" 등
}