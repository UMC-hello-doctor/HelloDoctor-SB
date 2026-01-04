package com.example.umc.domain.user.dto;

import com.example.umc.domain.user.enums.BloodType;
import com.example.umc.domain.user.enums.Gender;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private BloodType bloodType;
    private String bloodTypeDetail;
    private boolean hasAllergy;
    private List<String> allergies;
    private boolean takesMedication;
    private List<String> medications;
    private boolean isPregnant;
}