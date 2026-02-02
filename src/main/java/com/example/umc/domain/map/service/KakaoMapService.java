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
        log.info("📍 주소 변환 요청 - 위도(Lat): {}, 경도(Lon): {}", latitude, longitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // x=경도(lon), y=위도(lat) 순서 중요!
        String url = String.format("%s?x=%s&y=%s", KAKAO_URL, longitude, latitude);

        try {
            ResponseEntity<KakaoResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, KakaoResponseDto.class
            );

            KakaoResponseDto body = response.getBody();

            // [디버깅] 응답이 비었는지 확인
            if (body == null || body.getDocuments() == null || body.getDocuments().isEmpty()) {
                log.warn("⚠️ 카카오 API 응답은 성공했으나, 해당 좌표에 주소가 없습니다. (Body: {})", body);
                // 주소를 못 찾았을 때 서울로 퉁치지 말고, 로그를 보고 좌표를 수정하세요.
                return new String[]{"서울특별시", "중구"};
            }

            String city = body.getDocuments().get(0).getRegion1DepthName();
            String district = body.getDocuments().get(0).getRegion2DepthName();

            log.info("✅ 주소 변환 성공: {} {}", city, district);
            return new String[]{city, district};

        } catch (Exception e) {
            // [디버깅] 여기서 401이 뜨면 키 문제, 400이면 파라미터 문제
            log.error("❌ 카카오 API 호출 중 에러 발생!", e);
            log.error("❌ 카카오 에러 상세: {}", e.getMessage());
            // 에러가 났을 때 서울로 보내는 기본값 (테스트용)
            // 실제 운영시에는 에러를 던지는 게 낫습니다.
            return new String[]{"서울특별시", "중구"};
        }
    }
}