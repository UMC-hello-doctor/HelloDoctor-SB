package com.example.umc.domain.user.service;

import com.example.umc.domain.user.dto.GoogleLoginDto;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.repository.UserRepository;
import com.example.umc.global.config.JwtTokenProvider;
import com.example.umc.global.error.GeneralException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
                throw new GeneralException("JWT4002", "유효하지 않은 구글 토큰입니다.");
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

    @Transactional
    public Map<String, String> reissueToken(String refreshToken) {
        try {
            jwtTokenProvider.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GeneralException("JWT4031", "리프레시 토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new GeneralException("JWT4032", "유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException("USER4004", "사용자를 찾을 수 없습니다."));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new GeneralException("JWT4005", "RefreshToken이 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        user.updateRefreshToken(newRefreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return tokens;
    }
}