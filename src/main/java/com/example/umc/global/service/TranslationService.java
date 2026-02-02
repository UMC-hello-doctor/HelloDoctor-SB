package com.example.umc.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate;

    @Value("${DEEPL_API_KEY}")
    private String apiKey;

    private static final String DEEPL_URL = "https://api-free.deepl.com/v2/translate";

    /**
     * 텍스트 리스트를 한 번에 번역 (API 호출 횟수 절약)
     */
    public List<String> translate(List<String> texts, String targetLang) {
        // 한국어(KO)라면 번역 불필요
        if ("KO".equalsIgnoreCase(targetLang)) {
            return texts;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

            // DeepL 요청 바디 생성
            Map<String, Object> body = new HashMap<>();
            body.put("text", texts);
            body.put("target_lang", targetLang.toUpperCase()); // "VI", "EN" 등

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // 호출
            DeepLResponse response = restTemplate.postForObject(DEEPL_URL, entity, DeepLResponse.class);

            if (response != null && response.getTranslations() != null) {
                return response.getTranslations().stream()
                        .map(DeepLResponse.Translation::getText)
                        .toList();
            }
        } catch (Exception e) {
            log.error("번역 실패: {}", e.getMessage());
        }

        // 실패 시 원문 그대로 반환 (에러 방지)
        return texts;
    }

    // DeepL 응답용 내부 클래스
    @lombok.Getter
    @lombok.NoArgsConstructor
    static class DeepLResponse {
        private List<Translation> translations;
        @lombok.Getter @lombok.NoArgsConstructor
        static class Translation { private String text; private String detected_source_language; }
    }
}