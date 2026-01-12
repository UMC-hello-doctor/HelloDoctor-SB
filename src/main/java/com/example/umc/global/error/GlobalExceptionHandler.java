package com.example.umc.global.error;

import com.example.umc.global.common.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ApiResponse<Object> handleGeneralException(GeneralException e) {
        return ApiResponse.onFailure(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Object> handleAuthenticationException(AuthenticationException e) {
        return ApiResponse.onFailure("COMMON401", "인증이 필요합니다.", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Object> handleAccessDeniedException(AccessDeniedException e) {
        return ApiResponse.onFailure("COMMON403", "금지된 요청입니다.", null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> handleRuntimeException(RuntimeException e) {
        return ApiResponse.onFailure("COMMON400", e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.onFailure("COMMON500", "서버 에러, 관리자에게 문의 바랍니다.", null);
    }
}