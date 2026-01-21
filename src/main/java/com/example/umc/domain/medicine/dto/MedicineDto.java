package com.example.umc.domain.medicine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MedicineDto {

    @Getter
    @NoArgsConstructor
    public static class AddMedicineReq {
        private String medicineName;   // 약 이름
        private String medicineImage;  // 이미지
        private String medicineCode;   // 식별코드
        private String memo;           // "식후 30분" 등

        private List<AlarmReq> alarmList; // 알림 시간 목록
    }

    @Getter
    @NoArgsConstructor
    public static class AlarmReq {
        private String label;   // "아침"
        private String time;    // "09:00" (String으로 받아서 LocalTime 변환)
    }
}