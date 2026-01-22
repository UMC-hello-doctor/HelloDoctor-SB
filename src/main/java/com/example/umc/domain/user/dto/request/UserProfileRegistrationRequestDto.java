package com.example.umc.domain.user.dto.request;

import com.example.umc.domain.user.enums.AllergyType;
import com.example.umc.domain.user.enums.BloodType;
import com.example.umc.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRegistrationRequestDto {
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String displayName;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    @NotNull(message = "혈액형을 선택해주세요.")
    private BloodType bloodType;

    private String bloodTypeDetail;

    @NotNull(message = "알레르기 여부를 선택해주세요.")
    private Boolean hasAllergy;

    private List<AllergyType> allergyTypes;

    @NotNull(message = "복약 여부를 선택해주세요.")
    private Boolean takesMedication;

    @NotNull(message = "임신 여부를 선택해주세요.")
    private Boolean isPregnant;
}