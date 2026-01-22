package com.example.umc.domain.map.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Department {

    // === 의과 (Medical) ===
    INTERNAL_MEDICINE("내과", "D001"),
    PEDIATRICS("소아청소년과", "D002"),
    NEUROLOGY("신경과", "D003"),
    PSYCHIATRY("정신건강의학과", "D004"),
    DERMATOLOGY("피부과", "D005"),
    SURGERY("외과", "D006"),
    THORACIC_SURGERY("심장혈관흉부외과", "D007"),
    ORTHOPEDICS("정형외과", "D008"),
    NEUROSURGERY("신경외과", "D009"),
    PLASTIC_SURGERY("성형외과", "D010"),
    OBSTETRICS_GYNECOLOGY("산부인과", "D011"),
    OPHTHALMOLOGY("안과", "D012"),
    OTOLARYNGOLOGY("이비인후과", "D013"),
    UROLOGY("비뇨의학과", "D014"),
    TUBERCULOSIS("결핵과", "D015"),
    REHABILITATION("재활의학과", "D016"),
    ANESTHESIOLOGY("마취통증의학과", "D017"),
    RADIOLOGY("영상의학과", "D018"),
    RADIATION_ONCOLOGY("치료방사선과", "D019"),
    CLINICAL_PATHOLOGY("임상병리과", "D020"),
    ANATOMICAL_PATHOLOGY("해부병리과", "D021"),
    FAMILY_MEDICINE("가정의학과", "D022"),
    NUCLEAR_MEDICINE("핵의학과", "D023"),
    EMERGENCY_MEDICINE("응급의학과", "D024"),
    OCCUPATIONAL_MEDICINE("산업의학과", "D025"),
    PREVENTIVE_MEDICINE("예방의학과", "D029"),
    RAD_ONCOLOGY_2("방사선종양학과", "D031"), // D019와 유사하나 코드 분리됨
    PATHOLOGY("병리과", "D032"),
    LABORATORY_MEDICINE("진단검사의학과", "D033"),
    OCCUPATIONAL_ENV_MEDICINE("작업환경의학과", "D053"),

    // === 치과 (Dental) ===
    DENTISTRY("치과", "D026"),
    ORAL_MAXILLOFACIAL_SURGERY("구강악안면외과", "D034"),
    PROSTHODONTICS("치과보철과", "D035"),
    ORTHODONTICS("치과교정과", "D036"),
    PEDIATRIC_DENTISTRY("소아치과", "D037"),
    PERIODONTICS("치주과", "D038"),
    CONSERVATIVE_DENTISTRY("치과보존과", "D039"),
    ORAL_MEDICINE("구강내과", "D040"),
    ORAL_MAXILLOFACIAL_RADIOLOGY("구강악안면방사선과", "D041"),
    ORAL_PATHOLOGY("구강병리과", "D042"),
    PREVENTIVE_DENTISTRY("예방치과", "D043"),
    ORAL_FACIAL_SURGERY("구강안면외과", "D054"),
    DENTAL_IMAGING("영상치의학과", "D055"),
    INTEGRATED_DENTISTRY("통합치의학과", "D056"),

    // === 한방 (Korean Medicine) ===
    KOREAN_INTERNAL("한방내과", "D044"),
    KOREAN_OB_GYN("한방부인과", "D045"),
    KOREAN_PEDIATRICS("한방소아과", "D046"),
    KOREAN_ENT_DERMA("한방안이비인후피부과", "D047"),
    KOREAN_NEUROPSYCHIATRY("한방신경정신과", "D048"),
    KOREAN_REHABILITATION("한방재활의학과", "D049"),
    SASANG_CONSTITUTION("사상체질과", "D050"),
    ACUPUNCTURE("침구과", "D051"),
    KOREAN_EMERGENCY("한방응급과", "D052"),

    // === 기타/특수 (Others) ===
    MILITARY_MANPOWER("병무청", "D057"),
    MILITARY_MEDICAL("의무대", "D058"),
    SCHOOL_FACILITY("학교시설", "D059"),
    OTHERS("기타", "D060");

    private final String koreanName; // 앱 화면 표시용 이름
    private final String code;       // API 요청용 코드

    // 한글 이름으로 코드 찾기 (예: "내과" -> "D001")
    public static String getCodeByName(String name) {
        return Arrays.stream(values())
                .filter(dept -> dept.koreanName.equals(name))
                .findFirst()
                .map(Department::getCode)
                .orElse(null);
    }

    // 코드로 Enum 찾기 (예: "D001" -> Department.INTERNAL_MEDICINE)
    public static Department getByCode(String code) {
        return Arrays.stream(values())
                .filter(dept -> dept.code.equals(code))
                .findFirst()
                .orElse(Department.OTHERS); // 없으면 기타 반환
    }
}