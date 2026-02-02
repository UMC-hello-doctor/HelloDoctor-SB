package com.example.umc.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class PublicHospitalXmlDto {

    private Header header;
    private Body body;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Items items;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<Item> itemList;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        // 1. 기본 정보
        private String hpid;      // 기관ID
        private String dutyName;  // 병원 이름
        private Double distance;  // 거리 (일부 API에서는 없을 수 있음)
        private String dutyAddr;  // 주소
        private String dutyTel1;  // 전화번호
        private String dutyDivNam; // 병원분류명
        // 2. 좌표 정보 (XML 태그명: wgs84Lat, wgs84Lon)
        // [중요] 변수명은 latitude로 쓰되, XML의 wgs84Lat 값을 여기에 넣으라고 매핑해주는 것
        @JacksonXmlProperty(localName = "wgs84Lat")
        private Double latitude;

        @JacksonXmlProperty(localName = "wgs84Lon")
        private Double longitude;

        // 3. 진료 시간 정보 (월~일, 공휴일)
        // XML 태그 예: dutyTime1s(월요일 시작), dutyTime1c(월요일 마감)

        private String dutyTime1s; // 월요일 시작
        private String dutyTime1c; // 월요일 종료

        private String dutyTime2s; // 화요일 시작
        private String dutyTime2c; // 화요일 종료

        private String dutyTime3s; // 수요일 시작
        private String dutyTime3c; // 수요일 종료

        private String dutyTime4s; // 목요일 시작
        private String dutyTime4c; // 목요일 종료

        private String dutyTime5s; // 금요일 시작
        private String dutyTime5c; // 금요일 종료

        private String dutyTime6s; // 토요일 시작
        private String dutyTime6c; // 토요일 종료

        private String dutyTime7s; // 일요일 시작
        private String dutyTime7c; // 일요일 종료

        private String dutyTime8s; // 공휴일 시작
        private String dutyTime8c; // 공휴일 종료
    }
}