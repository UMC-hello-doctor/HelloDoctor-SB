package com.example.umc.domain.user.dto;

import com.example.umc.domain.user.enums.Language;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanguageUpdateDto {
    private Language language;
}