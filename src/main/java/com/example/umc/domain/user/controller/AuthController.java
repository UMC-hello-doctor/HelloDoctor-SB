package com.example.umc.domain.user.controller;

import com.example.umc.domain.user.dto.GoogleLoginDto;
import com.example.umc.domain.user.service.AuthService;
import com.example.umc.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ApiResponse<Map<String, String>> googleLogin(@RequestBody GoogleLoginDto dto) {
        Map<String, String> tokens = authService.googleLogin(dto);
        return ApiResponse.onSuccess(tokens);
    }

    @PostMapping("/reissue")
    public ApiResponse<Map<String, String>> reissueToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, String> tokens = authService.reissueToken(refreshToken);
        return ApiResponse.onSuccess(tokens);
    }
}