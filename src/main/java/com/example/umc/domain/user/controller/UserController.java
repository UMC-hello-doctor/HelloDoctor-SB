package com.example.umc.domain.user.controller;

import com.example.umc.domain.user.dto.LanguageUpdateDto; // DTO import 추가
import com.example.umc.domain.user.dto.UserRegistrationDto;
import com.example.umc.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 회원 정보 등록 API
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUserInfo(@RequestBody UserRegistrationDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.registerUserInfo(email, dto);

        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "사용자 정보 등록이 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 2. 토큰 재발급 API
    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        Map<String, String> tokens = userService.reissueToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    // 3. 언어 설정 변경 API
    @PatchMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(@RequestBody LanguageUpdateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // DTO에서 Enum을 꺼내 문자열로 변환하여 전달 (.name() 사용)
        userService.updateLanguage(email, dto.getLanguage().name());

        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "언어 설정이 변경되었습니다.");

        return ResponseEntity.ok(response);
    }
}