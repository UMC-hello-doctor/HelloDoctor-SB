package com.example.umc.domain.map.service;

import com.example.umc.domain.map.dto.MapDto;
import com.example.umc.domain.map.dto.PublicHospitalXmlDto;
import com.example.umc.domain.map.model.Department;
import com.example.umc.domain.map.util.MedicalCategoryMapper; // [필수] Mapper import 확인
import com.example.umc.global.service.TranslationService;
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
    private final TranslationService translationService;

    @Value("${open-api.service-key}")
    private String serviceKey;

    private static final String BASE_URL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";

    /**
     * [핵심 로직] 주소 + 진료과 + 언어설정 -> 병원 검색 및 번역
     */
    public List<MapDto.HospitalSimpleRes> searchHospitals(
            String city, String district, String deptName,
            Double userLat, Double userLon,
            String userLang
    ) {
        // 1. 진료과 한글명 -> 코드 변환
        String deptCode = Department.getCodeByName(deptName);
        if (deptCode == null) deptCode = "D001";

        List<MapDto.HospitalSimpleRes> hospitalList = new ArrayList<>();

        try {
            // 2. 공공데이터 API 호출
            String urlString = String.format("%s?serviceKey=%s&Q0=%s&Q1=%s&QD=%s&numOfRows=200&pageNo=1",
                    BASE_URL, serviceKey,
                    URLEncoder.encode(city, StandardCharsets.UTF_8),
                    URLEncoder.encode(district, StandardCharsets.UTF_8),
                    deptCode);

            URI uri = URI.create(urlString);
            PublicHospitalXmlDto response = restTemplate.getForObject(uri, PublicHospitalXmlDto.class);

            if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                return new ArrayList<>();
            }

            // 3. 데이터 매핑 (기본 한국어 리스트 생성)
            hospitalList = response.getBody().getItems().getItemList().stream()
                    .map(item -> {
                        double hospLat = (item.getLatitude() != null) ? item.getLatitude() : 0.0;
                        double hospLon = (item.getLongitude() != null) ? item.getLongitude() : 0.0;

                        // 거리 계산 (미터 단위는 DistanceUtils 내부에서 처리됨)
                        double dist = DistanceUtils.calculateDistance(userLat, userLon, hospLat, hospLon);

                        String startTime = formatTime(item.getDutyTime1s());
                        String endTime = formatTime(item.getDutyTime1c());
                        String hours = (startTime != null && endTime != null) ? startTime + " ~ " + endTime : "정보 없음";

                        return MapDto.HospitalSimpleRes.builder()
                                .id(item.getHpid())
                                .name(item.getDutyName())
                                .category(item.getDutyDivNam()) // 카테고리 추가
                                .address(item.getDutyAddr())
                                .tel(item.getDutyTel1())
                                .distance(dist)
                                .latitude(hospLat)
                                .longitude(hospLon)
                                .businessHours(hours)
                                .build();
                    })
                    .sorted(Comparator.comparingDouble(MapDto.HospitalSimpleRes::getDistance))
                    .limit(20)
                    .collect(Collectors.toList());

            // ==========================================
            // [NEW] 4. 번역 로직 (리스트 생성 후 실행해야 함)
            // ==========================================
            if (!"KO".equalsIgnoreCase(userLang) && !hospitalList.isEmpty()) {

                // (1) 병원 이름 번역 (API 호출)
                List<String> namesToTranslate = hospitalList.stream()
                        .map(MapDto.HospitalSimpleRes::getName)
                        .collect(Collectors.toList());

                List<String> translatedNames = translationService.translate(namesToTranslate, userLang);

                // (2) 결과 리스트 업데이트
                for (int i = 0; i < hospitalList.size(); i++) {
                    MapDto.HospitalSimpleRes hospital = hospitalList.get(i);

                    // 이름 교체
                    if (i < translatedNames.size()) {
                        hospital.setName(translatedNames.get(i));
                    }

                    // 카테고리 교체 (정적 매핑)
                    // MedicalCategoryMapper가 없으면 에러나므로 확인 필요
                    String translatedCategory = MedicalCategoryMapper.translate(hospital.getCategory(), userLang);
                    hospital.setCategory(translatedCategory);
                }
            }

            return hospitalList;

        } catch (Exception e) {
            log.error("병원 검색 실패", e);
            throw new RuntimeException("병원 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * [Wrapper] 좌표 -> 주소 변환 후 검색 호출
     */
    public List<MapDto.HospitalSimpleRes> searchHospitalsByLocation(
            Double userLat, Double userLon, String deptName, String userLang
    ) {
        // 1. 내 좌표 -> 주소 변환
        String[] address = kakaoMapService.getAddress(userLat, userLon);
        String city = address[0];
        String district = address[1];

        // 2. 검색 실행 (userLang 전달)
        return searchHospitals(city, district, deptName, userLat, userLon, userLang);
    }

    private String formatTime(String time) {
        if (time == null || time.length() != 4) return null;
        return time.substring(0, 2) + ":" + time.substring(2, 4);
    }
}