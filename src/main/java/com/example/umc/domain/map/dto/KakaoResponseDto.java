package com.example.umc.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class KakaoResponseDto {

    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class Document {
        // 카카오가 주는 변수명은 스네이크 케이스라 매핑 필요

        @JsonProperty("region_1depth_name")
        private String region1DepthName; // 예: 서울특별시

        @JsonProperty("region_2depth_name")
        private String region2DepthName; // 예: 종로구
    }
}