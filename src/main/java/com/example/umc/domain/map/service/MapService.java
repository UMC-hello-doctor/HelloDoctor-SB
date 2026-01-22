package com.example.umc.domain.map.service;

import com.example.umc.domain.map.dto.MapDto;
import com.example.umc.domain.map.dto.PublicHospitalXmlDto;
import com.example.umc.domain.map.model.Department;
import com.example.umc.global.util.DistanceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {

    private final RestTemplate restTemplate;
    private final KakaoMapService kakaoMapService;

    @Value("${open-api.service-key}")
    private String serviceKey;

    // 주소 기반 목록 조회 API (Op.1)
    private static final String BASE_URL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";

    /**
     * [핵심 로직] 주소(시/도, 시/군/구)와 진료과, 기준 좌표를 받아 병원을 검색하고 정렬함
     */
    public List<MapDto.HospitalSimpleRes> searchHospitals(
            String city, String district, String deptName,
            Double userLat, Double userLon
    ) {
        // 1. 진료과 한글명 -> 코드 변환 (Enum)
        String deptCode = Department.getCodeByName(deptName);
        if (deptCode == null) deptCode = "D001"; // 기본값(내과)

        try {
            // 2. 공공데이터 URL 생성 (주소 인코딩 필수)
            String urlString = String.format("%s?serviceKey=%s&Q0=%s&Q1=%s&QD=%s&numOfRows=200&pageNo=1",
                    BASE_URL, serviceKey,
                    URLEncoder.encode(city, StandardCharsets.UTF_8),
                    URLEncoder.encode(district, StandardCharsets.UTF_8),
                    deptCode);

            log.info("Request URL: {}", urlString);

            URI uri = URI.create(urlString);
            PublicHospitalXmlDto response = restTemplate.getForObject(uri, PublicHospitalXmlDto.class);

            if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                return new ArrayList<>();
            }

            // 3. 데이터 매핑 + 거리 계산 + 정렬
            return response.getBody().getItems().getItemList().stream()
                    .map(item -> {
                        // (1) 좌표 파싱 (null 안전 처리)
                        double hospLat = (item.getLatitude() != null) ? item.getLatitude() : 0.0;
                        double hospLon = (item.getLongitude() != null) ? item.getLongitude() : 0.0;

                        // (2) 거리 계산
                        double dist = DistanceUtils.calculateDistance(userLat, userLon, hospLat, hospLon);

                        // (3) 영업시간 포맷팅
                        String startTime = formatTime(item.getDutyTime1s());
                        String endTime = formatTime(item.getDutyTime1c());
                        String hours = (startTime != null && endTime != null)
                                ? startTime + " ~ " + endTime
                                : "정보 없음";

                        // (4) DTO 변환
                        return MapDto.HospitalSimpleRes.builder()
                                .id(item.getHpid())
                                .name(item.getDutyName())
                                .address(item.getDutyAddr())
                                .tel(item.getDutyTel1())
                                .distance(dist)
                                .latitude(hospLat)
                                .longitude(hospLon)
                                .businessHours(hours)
                                .build();
                    })
                    // 거리순 정렬 후 상위 20개 반환
                    .sorted(Comparator.comparingDouble(MapDto.HospitalSimpleRes::getDistance))
                    .limit(20)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("병원 검색 실패", e);
            throw new RuntimeException("병원 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * [Wrapper] 좌표(위도, 경도)를 받아 주소로 변환한 뒤, 위 핵심 로직을 호출함
     */
    public List<MapDto.HospitalSimpleRes> searchHospitalsByLocation(
            Double userLat, Double userLon, String deptName
    ) {
        // 1. 내 좌표 -> 주소 변환 ("서울특별시", "종로구")
        String[] address = kakaoMapService.getAddress(userLat, userLon);
        String city = address[0];
        String district = address[1];

        // 2. 변환된 주소로 검색 실행 (재활용)
        // [수정] 파라미터 순서와 개수를 searchHospitals 정의와 맞췄습니다.
        return searchHospitals(city, district, deptName, userLat, userLon);
    }

    // 시간 포맷팅 (0900 -> 09:00)
    private String formatTime(String time) {
        if (time == null || time.length() != 4) return null;
        return time.substring(0, 2) + ":" + time.substring(2, 4);
    }
}