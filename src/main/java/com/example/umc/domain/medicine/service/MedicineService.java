package com.example.umc.domain.medicine.service;

import com.example.umc.domain.medicine.dto.MedicineDto;
import com.example.umc.domain.medicine.dto.MedicineSearchDto;
import com.example.umc.domain.medicine.entity.MedicineAlarm;
import com.example.umc.domain.medicine.entity.MyMedicine;
import com.example.umc.domain.medicine.repository.MyMedicineRepository;
import com.example.umc.domain.user.entity.PatientProfile;
import com.example.umc.domain.user.entity.User;
import com.example.umc.domain.user.repository.PatientProfileRepository;
import com.example.umc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // [수정 3] 로그 사용을 위해 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineService {

    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final MyMedicineRepository myMedicineRepository;
    private final RestTemplate restTemplate;

    @Value("${open-api.service-key}")
    private String serviceKey;

    //  'e약은요(개요정보)' API (DTO와 호환성 맞춤)
    private static final String DRUG_SEARCH_URL = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList";

    /**
     * [POST] 복용 약 추가하기 (DB 저장)
     */
    @Transactional
    public Long addMyMedicine(Long userId, MedicineDto.AddMedicineReq req) {

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // 2. 환자 프로필 조회
        PatientProfile profile = patientProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("해당 유저의 환자 프로필이 없습니다. 먼저 프로필을 생성해주세요."));

        // 3. 약 정보 생성
        MyMedicine myMedicine = MyMedicine.builder()
                .patientProfile(profile)
                .medicineName(req.getMedicineName())
                .medicineImage(req.getMedicineImage())
                .medicineCode(req.getMedicineCode())
                .memo(req.getMemo())
                .build();

        // 4. 알림 시간 추가
        if (req.getAlarmList() != null) {
            req.getAlarmList().forEach(alarmReq -> {
                MedicineAlarm alarm = MedicineAlarm.builder()
                        .label(alarmReq.getLabel())
                        .time(LocalTime.parse(alarmReq.getTime()))
                        .build();
                // 연관관계 설정
                alarm.setMyMedicine(myMedicine);
                myMedicine.getAlarms().add(alarm);
            });
        }

        // 5. 저장
        myMedicineRepository.save(myMedicine);

        return myMedicine.getId();
    }

    /**
     * [GET] 공공데이터 약 검색
     */
    public List<MedicineSearchDto.Item> searchMedicine(String keyword) {
        try {
            //  (DRUG_SEARCH_URL 사용)
            String urlString = String.format("%s?serviceKey=%s&itemName=%s&numOfRows=20&pageNo=1&type=xml",
                    DRUG_SEARCH_URL,
                    serviceKey,
                    URLEncoder.encode(keyword, StandardCharsets.UTF_8));

            URI uri = URI.create(urlString);

            MedicineSearchDto response = restTemplate.getForObject(uri, MedicineSearchDto.class);

            if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                return new ArrayList<>();
            }

            return response.getBody().getItems().getItemList();

        } catch (Exception e) {
            log.error("공공데이터 약 검색 실패", e);
            throw new RuntimeException("약 검색 중 오류가 발생했습니다.");
        }
    }
}