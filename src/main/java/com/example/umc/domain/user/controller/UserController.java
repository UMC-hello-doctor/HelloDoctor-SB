package com.example.umc.domain.user.controller;

import com.example.umc.domain.user.dto.request.LanguageUpdateDto;
import com.example.umc.domain.user.dto.request.UserProfileRegistrationRequestDto;
import com.example.umc.domain.user.dto.response.MyPageResponseDto;
import com.example.umc.domain.user.dto.response.UserProfileRegistrationResponseDto;
import com.example.umc.domain.user.service.UserService;
import com.example.umc.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/language")
    public ApiResponse<String> updateLanguage(@RequestAttribute("userId") Long userId, @RequestBody LanguageUpdateDto dto) {
        userService.updateLanguage(userId, dto);
        return ApiResponse.onSuccess("언어 설정이 변경되었습니다.", null);
    }

    @PostMapping("/profiles")
    public ApiResponse<UserProfileRegistrationResponseDto> registerProfile(
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid UserProfileRegistrationRequestDto dto) {
        UserProfileRegistrationResponseDto response = userService.registerUserProfile(userId, dto);
        return ApiResponse.onSuccess("건강 프로필이 저장되었습니다.", response);
    }

    @GetMapping("/profiles/me")
    public ApiResponse<MyPageResponseDto> getMyPageProfile(@RequestAttribute("userId") Long userId) {
        MyPageResponseDto response = userService.getMyPageProfile(userId);
        return ApiResponse.onSuccess("프로필 조회 성공", response);
    }
}