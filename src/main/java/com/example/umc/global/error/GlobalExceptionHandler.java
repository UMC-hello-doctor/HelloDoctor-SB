package com.example.umc.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {

        Map<String, Object> response = new HashMap<>();
        response.put("code", "ERROR");
        response.put("message", e.getMessage());

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SERVER_ERROR");
        response.put("message", "서버 내부 오류가 발생했습니다.");

        e.printStackTrace();

        return ResponseEntity.status(500).body(response);
    }
}