package com.example.umc.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllergyType {
    ANTIBIOTIC("ANTIBIOTIC", "항생제"),
    PAINKILLER("PAINKILLER","소염진통제"),
    VACCINE("VACCINE","백신 성분"),
    ANESTHETIC("ANESTHETIC","국소 마취제"),
    OTHER("OTHER", "기타");

    private final String code;
    private final String description;
}