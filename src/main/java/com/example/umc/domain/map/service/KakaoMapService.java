package com.example.umc.domain.map.service;

import com.example.umc.domain.map.dto.KakaoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private static final String KAKAO_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public String[] getAddress(Double latitude, Double longitude) {
        // 1. 헤더 설정 (Authorization: KakaoAK {KEY})
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 2. URL 파라미터 설정 (x=경도, y=위도) *주의: 카카오는 x가 경도(lon), y가 위도(lat)
        String url = String.format("%s?x=%s&y=%s", KAKAO_URL, longitude, latitude);

        try {
            // 3. 호출
            ResponseEntity<KakaoResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, KakaoResponseDto.class
            );

            KakaoResponseDto body = response.getBody();

            if (body != null && !body.getDocuments().isEmpty()) {
                // documents[0] = 행정동, documents[1] = 법정동 (보통 0번 쓰면 됨)
                String city = body.getDocuments().get(0).getRegion1DepthName();    // 서울특별시
                String district = body.getDocuments().get(0).getRegion2DepthName(); // 종로구

                log.info("📍 카카오 주소 변환 성공: {} {}", city, district);
                return new String[]{city, district};
            }

        } catch (Exception e) {
            log.error("카카오 API 호출 실패", e);
        }

        // 실패 시 기본값 or 에러 처리
        return new String[]{"서울특별시", "중구"};
    }
}