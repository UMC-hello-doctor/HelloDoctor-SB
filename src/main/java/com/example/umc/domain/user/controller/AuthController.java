package com.example.umc.domain.user.controller;

import com.example.umc.domain.user.dto.GoogleLoginDto;
import com.example.umc.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> googleLogin(@RequestBody GoogleLoginDto dto) {
        Map<String, String> tokens = authService.googleLogin(dto);
        return ResponseEntity.ok(tokens);
    }
}