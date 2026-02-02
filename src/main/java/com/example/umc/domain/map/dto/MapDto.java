package com.example.umc.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

public class MapDto {

    // [요청] 프론트 -> 백엔드
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationReq {
        private Double latitude;
        private Double longitude;
    }

    // [응답] 백엔드 -> 프론트 (리스트용)
    @Getter
    @Setter
    @Builder
    public static class HospitalSimpleRes {
        private String id;        // hpid
        private String name;      // 병원명
        private String category;  // 병원 분류
        private String address;   // 주소 (추가)
        private String tel;       // 전화번호 (추가)
        private Double distance;  // 거리 (km)
        private Double latitude;  // 위도
        private Double longitude; // 경도
        private String businessHours;
    }
}