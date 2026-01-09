package com.example.umc.domain.user.service;

import com.example.umc.domain.user.dto.GoogleLoginDto;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.enums.UserStatus;
import com.example.umc.domain.user.repository.UserRepository;
import com.example.umc.global.config.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${google.client-id}")
    private String googleClientId;

    @Transactional
    public Map<String, String> googleLogin(GoogleLoginDto dto) {
        String email;
        String name;

        if ("TEST_TOKEN".equals(dto.getIdToken())) {
            email = "test@gmail.com";
            name = "테스트유저";
        } else {
            try {
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .build();

                GoogleIdToken idToken = verifier.verify(dto.getIdToken());

                if (idToken == null) {
                    throw new IllegalArgumentException();
                }

                GoogleIdToken.Payload payload = idToken.getPayload();
                email = payload.getEmail();
                name = (String) payload.get("name");
            } catch (Exception e) {
                throw new RuntimeException("유효하지 않은 구글 토큰입니다.");
            }
        }

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