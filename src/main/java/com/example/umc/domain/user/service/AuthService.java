package com.example.umc.domain.user.service;

import com.example.umc.domain.user.dto.GoogleLoginDto;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.enums.UserStatus;
import com.example.umc.domain.user.repository.UserRepository;
import com.example.umc.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Map<String, String> googleLogin(GoogleLoginDto dto) {
        String email;
        String name;

        // 스웨거 테스트용
        if ("TEST_TOKEN".equals(dto.getIdToken())) {
            email = "test@gmail.com";
            name = "테스트유저";
        } else {
            // 원래 로직
            String googleApiUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + dto.getIdToken();
            RestTemplate restTemplate = new RestTemplate();
            try {
                Map<String, Object> googleResponse = restTemplate.getForObject(googleApiUrl, Map.class);
                email = (String) googleResponse.get("email");
                name = (String) googleResponse.get("name");
            } catch (Exception e) {
                throw new RuntimeException("유효하지 않은 구글 토큰입니다.");
            }
        }

        // DB에서 회원 조회 or 가입
        User user = userRepository.findByEmail(email)
                .map(entity -> {
                    entity.setName(name);
                    return entity;
                })
                .orElseGet(() -> User.builder()
                        .email(email)
                        .name(name)
                        .status(UserStatus.PENDING)
                        .build());

        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        user.updateRefreshToken(refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }
}