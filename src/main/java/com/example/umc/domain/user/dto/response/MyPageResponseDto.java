package com.example.umc.domain.user.dto.response;

import com.example.umc.domain.user.enums.BloodType;
import com.example.umc.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String displayName;
    private LocalDate birthDate;
    private Gender gender;
    private BloodType bloodType;
    private List<String> tags;
}