package com.example.umc.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    KOREAN("ko", "한국어"),
    ENGLISH("en", "영어 (English)"),
    JAPANESE("ja", "일본어 (日本語)"),
    CHINESE("zh", "중국어 (中文)"),
    VIETNAMESE("vi", "베트남어 (tiếng Việt)");

    private final String code;
    private final String displayName;
}
