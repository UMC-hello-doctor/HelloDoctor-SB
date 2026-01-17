package com.example.umc.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "email", "name", "accessToken", "refreshToken"})
public class LoginResponseDto {
    private Long id;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
}