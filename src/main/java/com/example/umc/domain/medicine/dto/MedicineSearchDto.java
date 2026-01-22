package com.example.umc.domain.medicine.dto;

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
public class MedicineSearchDto {

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
        private int totalCount;
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

        // 1. 제품명/성분명 (XML 태그: itemName)
        @JacksonXmlProperty(localName = "itemName")
        private String medicineName;

        // 2. 식별표시 -> ID로 사용 (XML 태그: itemSeq)
        @JacksonXmlProperty(localName = "itemSeq")
        private String medicineId;

        // 3. 식별이미지 (XML 태그: itemImage)
        @JacksonXmlProperty(localName = "itemImage")
        private String medicineImage;

        // 업체명 (필요하다면 유지, 화면에 안 띄울 거면 생략 가능)
        @JacksonXmlProperty(localName = "entpName")
        private String entpName;

        // 효능 (필요하다면 유지)
        @JacksonXmlProperty(localName = "efcyQesitm")
        private String efficacy;
    }
}