package com.example.umc.domain.map.controller;

import com.example.umc.global.common.ApiResponse;
import com.example.umc.domain.map.dto.MapDto;
import com.example.umc.domain.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    /**
     * 위치 기반 병원 검색 API (다국어 지원)
     * GET /api/v1/places/search?lat=37.5&lon=127.0&dept=내과&lang=VI
     */
    @GetMapping("/search")
    public ApiResponse<List<MapDto.HospitalSimpleRes>> searchHospitals(
            @RequestParam("lat") Double userLat,
            @RequestParam("lon") Double userLon,
            @RequestParam("dept") String dept,
            @RequestParam(value = "lang", defaultValue = "KO") String lang // 언어 설정 받기
    ) {
        // [수정] 서비스 호출 시 'lang' 변수도 같이 넘겨줍니다.
        List<MapDto.HospitalSimpleRes> result = mapService.searchHospitalsByLocation(userLat, userLon, dept, lang);

        return ApiResponse.onSuccess(result);
    }
}