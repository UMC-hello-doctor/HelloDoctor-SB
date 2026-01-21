package com.example.umc.domain.medicine.controller;

import com.example.umc.global.common.ApiResponse;
import com.example.umc.domain.medicine.dto.MedicineDto;
import com.example.umc.domain.medicine.dto.MedicineSearchDto;
import com.example.umc.domain.medicine.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    /**
     * 1. [약 검색 API]
     * 사용자가 입력한 이름으로 약을 검색합니다.
     * 반환값: 식별이미지, 식별표시(ID), 제품명/성분명
     */
    @Operation(summary = "약 이름 검색", description = "약 이름을 입력받아 이미지, ID, 제품명을 반환합니다.")
    @GetMapping("/search")
    public ApiResponse<List<MedicineSearchDto.Item>> searchMedicines(
            @RequestParam("name") String name
    ) {
        List<MedicineSearchDto.Item> result = medicineService.searchMedicine(name);
        return ApiResponse.onSuccess(result);
    }

    /**
     * 2. [약 저장 API]
     * 검색된 약 정보와 알림 시간을 내 약통에 저장합니다.
     */
    @Operation(summary = "복용 약 추가하기", description = "선택한 약과 알림 시간을 저장합니다.")
    @PostMapping
    public ApiResponse<Long> addMedicine(
            @RequestParam Long userId,
            @RequestBody MedicineDto.AddMedicineReq req
    ) {
        Long medicineId = medicineService.addMyMedicine(userId, req);
        return ApiResponse.onSuccess(medicineId);
    }
}