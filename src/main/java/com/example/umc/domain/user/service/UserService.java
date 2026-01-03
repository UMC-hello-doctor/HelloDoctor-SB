package com.example.umc.domain.user.service;

import com.example.umc.global.config.JwtTokenProvider;
import com.example.umc.domain.user.dto.UserRegistrationDto;
import com.example.umc.domain.user.entity.Allergy;
import com.example.umc.domain.user.entity.Medication;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.enums.Language; // Enum 경로 확인 필요
import com.example.umc.domain.user.enums.UserStatus;
import com.example.umc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 회원 정보 등록
    public void registerUserInfo(String email, UserRegistrationDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // 기본 정보 업데이트
        user.setName(dto.getName());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());
        user.setBloodType(dto.getBloodType());
        user.setBloodTypeDetail(dto.getBloodTypeDetail());
        user.setHasAllergy(dto.isHasAllergy());
        user.setTakesMedication(dto.isTakesMedication());
        user.setPregnant(dto.isPregnant());
        user.setStatus(UserStatus.ACTIVE);

        // 1:N 관계 데이터 저장
        if (dto.isHasAllergy() && dto.getAllergies() != null) {
            user.getAllergies().clear();
            for (String allergyName : dto.getAllergies()) {
                user.getAllergies().add(Allergy.builder().name(allergyName).user(user).build());
            }
        }

        if (dto.isTakesMedication() && dto.getMedications() != null) {
            user.getMedications().clear();
            for (String medName : dto.getMedications()) {
                user.getMedications().add(Medication.builder().name(medName).user(user).build());
            }
        }
        userRepository.save(user);
    }

    // 2. 토큰 재발급
    public Map<String, String> reissueToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        user.updateRefreshToken(newRefreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return tokens;
    }

    // 3. 언어 설정 변경
    public void updateLanguage(String email, String languageCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        try {
            user.setLanguage(Language.valueOf(languageCode.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("지원하지 않는 언어입니다.");
        }
    }
}