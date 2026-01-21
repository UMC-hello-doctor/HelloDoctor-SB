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

        @GetMapping("/search")
    public ApiResponse<List<MapDto.HospitalSimpleRes>> searchHospitals(
            @RequestParam("lat") Double userLat,
            @RequestParam("lon") Double userLon,
            @RequestParam("dept") String dept
    ) {
        // Controller가 훨씬 깔끔해졌죠?
        List<MapDto.HospitalSimpleRes> result = mapService.searchHospitalsByLocation(userLat, userLon, dept);
        return ApiResponse.onSuccess(result);
    }
}