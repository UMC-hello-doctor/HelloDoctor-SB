//package com.example.umc.domain.user.service;
//
//import com.example.umc.domain.user.dto.UserRegistrationDto;
//import com.example.umc.domain.user.entity.*;
//import com.example.umc.domain.user.enums.AllergyType;
//import com.example.umc.domain.user.enums.Language;
//import com.example.umc.domain.user.repository.PatientProfileRepository;
//import com.example.umc.domain.user.repository.UserRepository;
//import com.example.umc.global.config.JwtTokenProvider;
//import com.example.umc.global.error.GeneralException;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PatientProfileRepository patientProfileRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    public void registerUserInfo(String email, UserRegistrationDto dto) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));
//
//        PatientProfile profile = patientProfileRepository.findByUser(user)
//                .orElseGet(() -> PatientProfile.builder()
//                        .user(user)
//                        .displayName(dto.getName())
//                        .gender(dto.getGender())
//                        .birthDate(dto.getBirthDate())
//                        .bloodType(dto.getBloodType())
//                        .build());
//
//        profile.setDisplayName(dto.getName());
//        profile.setGender(dto.getGender());
//        profile.setBirthDate(dto.getBirthDate());
//        profile.setBloodType(dto.getBloodType());
//        profile.setBloodTypeData(dto.getBloodTypeDetail());
//        profile.setHasAllergy(dto.isHasAllergy());
//        profile.setTakesMedication(dto.isTakesMedication());
//        profile.setPregnant(dto.isPregnant());
//
//        if (dto.isHasAllergy() && dto.getAllergies() != null) {
//            profile.getAllergies().clear();
//            for (String allergyName : dto.getAllergies()) {
//                try {
//                    AllergyType type = AllergyType.valueOf(allergyName);
//                    UserAllergy userAllergy = UserAllergy.builder()
//                            .patientProfile(profile)
//                            .allergyType(type)
//                            .build();
//                    profile.getAllergies().add(userAllergy);
//                } catch (IllegalArgumentException e) {
//                }
//            }
//        }
//
//        patientProfileRepository.save(profile);
//    }
//
//    public Map<String, String> reissueToken(String refreshToken) {
//        try {
//            jwtTokenProvider.validateToken(refreshToken);
//        } catch (ExpiredJwtException e) {
//            throw new GeneralException("JWT4031", "리프레시 토큰이 만료되었습니다.");
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new GeneralException("JWT4032", "유효하지 않은 리프레시 토큰입니다.");
//        }
//
//        String email = jwtTokenProvider.getEmail(refreshToken);
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));
//
//        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
//            throw new GeneralException("JWT4005", "RefreshToken이 일치하지 않습니다.");
//        }
//
//        String newAccessToken = jwtTokenProvider.createAccessToken(email);
//        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);
//
//        user.updateRefreshToken(newRefreshToken);
//
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", newAccessToken);
//        tokens.put("refreshToken", newRefreshToken);
//
//        return tokens;
//    }
//
//    public void updateLanguage(String email, String languageCode) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));
//
//        try {
//            user.setLanguage(Language.valueOf(languageCode.toUpperCase()));
//        } catch (IllegalArgumentException e) {
//            throw new GeneralException("COMMON400", "지원하지 않는 언어 코드입니다.");
//        }
//    }
//}