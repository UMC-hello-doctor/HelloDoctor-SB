package com.example.umc.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "email", "name", "accessToken", "refreshToken", "isNewUser"})
public class LoginResponseDto {
    private Long id;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;

    @JsonProperty("isNewUser")
    private boolean newUser; //true -> 프로필 입력 화면
}