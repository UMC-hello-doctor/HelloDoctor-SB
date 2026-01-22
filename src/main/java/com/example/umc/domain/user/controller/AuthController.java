package com.example.umc.domain.user.controller;

import com.example.umc.domain.user.dto.request.LoginRequestDto;
import com.example.umc.domain.user.dto.response.LoginResponseDto;
import com.example.umc.domain.user.service.AuthService;
import com.example.umc.global.common.ApiResponse;
import com.example.umc.domain.user.dto.request.RefreshTokenDto;
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
    public ApiResponse<LoginResponseDto> googleLogin(@RequestBody LoginRequestDto dto) {
        LoginResponseDto loginResponse = authService.googleLogin(dto);
        return ApiResponse.onSuccess(loginResponse);
    }

    @PostMapping("/reissue")
    public ApiResponse<Map<String, String>> reissueToken(@RequestBody RefreshTokenDto request) {
        String refreshToken = request.getRefreshToken();
        Map<String, String> tokens = authService.reissueToken(refreshToken);
        return ApiResponse.onSuccess(tokens);
    }
}