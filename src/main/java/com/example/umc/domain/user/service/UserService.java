package com.example.umc.domain.user.service;

import com.example.umc.domain.user.dto.request.LanguageUpdateDto;
import com.example.umc.domain.user.dto.request.UserProfileRegistrationRequestDto;
import com.example.umc.domain.user.dto.response.MyPageResponseDto;
import com.example.umc.domain.user.dto.response.UserProfileRegistrationResponseDto;
import com.example.umc.domain.user.entity.PatientProfile;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.entity.UserAllergy;
import com.example.umc.domain.user.enums.BloodType;
import com.example.umc.domain.user.repository.PatientProfileRepository;
import com.example.umc.domain.user.repository.UserRepository;
import com.example.umc.global.error.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;

    @Transactional
    public void updateLanguage(Long userId, LanguageUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));
        user.setLanguage(dto.getLanguage());
    }

    @Transactional
    public UserProfileRegistrationResponseDto registerUserProfile(Long userId, UserProfileRegistrationRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));

        if (dto.getBloodType() == BloodType.ETC &&
                (dto.getBloodTypeDetail() == null || dto.getBloodTypeDetail().trim().isEmpty())) {
            throw new GeneralException("PROFILE4000", "혈액형 상세 정보를 입력해주세요.");
        }

        if (dto.getBloodType() != BloodType.ETC && dto.getBloodTypeDetail() != null) {
            throw new GeneralException("PROFILE4000", "일반 혈액형 선택 시 상세 정보를 입력할 수 없습니다.");
        }

        if (dto.getHasAllergy() && (dto.getAllergyTypes() == null || dto.getAllergyTypes().isEmpty())) {
            throw new GeneralException("PROFILE4000", "알레르기 종류를 선택해주세요.");
        }

        String finalBloodDetail = (dto.getBloodType() == BloodType.ETC) ? dto.getBloodTypeDetail() : null;

        PatientProfile profile = PatientProfile.builder()
                .user(user)
                .displayName(dto.getDisplayName())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .bloodType(dto.getBloodType())
                .bloodTypeDetail(finalBloodDetail)
                .hasAllergy(dto.getHasAllergy())
                .takesMedication(dto.getTakesMedication())
                .isPregnant(dto.getIsPregnant())
                .build();

        if (dto.getHasAllergy() && dto.getAllergyTypes() != null) {
            dto.getAllergyTypes().forEach(type -> {
                UserAllergy allergy = UserAllergy.builder()
                        .allergyType(type)
                        .patientProfile(profile)
                        .build();
                profile.addUserAllergy(allergy);
            });
        }

        PatientProfile savedProfile = patientProfileRepository.save(profile);

        return UserProfileRegistrationResponseDto.builder()
                .profileId(savedProfile.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPageProfile(Long userId) {
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException("USER4006", "건강 프로필을 찾을 수 없습니다."));

        List<String> tags = profile.getUserAllergies().stream()
                .map(userAllergy -> userAllergy.getAllergyType().getDescription())
                .collect(Collectors.toList());

        return MyPageResponseDto.builder()
                .displayName(profile.getDisplayName())
                .birthDate(profile.getBirthDate())
                .gender(profile.getGender())
                .bloodType(profile.getBloodType())
                .tags(tags)
                .build();
    }
}