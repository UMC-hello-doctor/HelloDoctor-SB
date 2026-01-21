package com.example.umc.domain.medicine.entity;

import com.example.umc.domain.user.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "my_medicine")
public class MyMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PatientProfile과 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_profile_id", nullable = false)
    private PatientProfile patientProfile;

    @Column(nullable = false)
    private String medicineName;

    private String medicineImage;
    private String medicineCode;
    private String memo;

    @Builder.Default
    @OneToMany(mappedBy = "myMedicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineAlarm> alarms = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addAlarm(MedicineAlarm alarm) {
        this.alarms.add(alarm);
        alarm.setMyMedicine(this);
    }
}