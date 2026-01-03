package com.example.umc.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            // 토큰이 있는 경우 검증 시작
            if (token != null) {
                // 1. 토큰 검증 (여기서 에러가 나면 바로 catch문으로 이동)
                jwtTokenProvider.validateToken(token);

                // 2. 검증 통과 시 인증 객체 생성 및 저장
                String email = jwtTokenProvider.getEmail(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException e) {
            // [상세 처리 1] 토큰이 만료된 경우 -> 안드로이드에게 "재발급 요청해!"라고 알려줌
            setErrorResponse(response, "TOKEN_EXPIRED", "토큰이 만료되었습니다. Refresh Token을 보내주세요.");
            return; // 다음 필터로 진행하지 않고 여기서 요청 종료
        } catch (JwtException | IllegalArgumentException e) {
            // [상세 처리 2] 토큰이 위조되거나 형식이 이상한 경우 -> "로그인 다시 해!"
            setErrorResponse(response, "INVALID_TOKEN", "유효하지 않은 토큰입니다.");
            return; // 요청 종료
        }

        // 토큰이 없거나 검증에 성공한 경우 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    // JSON 에러 응답을 만드는 헬퍼 메서드
    private void setErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", code);
        responseMap.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }

    // 헤더에서 Bearer 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}