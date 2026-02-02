package com.example.umc.domain.map.util;

import java.util.HashMap;
import java.util.Map;

public class MedicalCategoryMapper {

    private static final Map<String, String> koToVi = new HashMap<>();

    static {
        koToVi.put("내과", "Nội khoa");
        koToVi.put("외과", "Ngoại khoa");
        koToVi.put("소아청소년과", "Nhi khoa");
        koToVi.put("피부과", "Da liễu");
        koToVi.put("치과", "Nha khoa");
        koToVi.put("한의원", "Y học cổ truyền");
        koToVi.put("종합병원", "Bệnh viện đa khoa");
        koToVi.put("의원", "Phòng khám");
        // ... 필요한 만큼 추가
    }

    public static String translate(String category, String lang) {
        if ("VI".equalsIgnoreCase(lang)) {
            return koToVi.getOrDefault(category, category); // 없으면 한국어 그대로
        }
        return category; // 기본값 한국어
    }
}