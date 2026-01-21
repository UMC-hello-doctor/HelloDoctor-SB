package com.example.umc.domain.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "medicine_alarm")
public class MedicineAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_medicine_id", nullable = false)
    private MyMedicine myMedicine;

    private String label;      // 라벨 (예: 아침, 점심, 저녁)

    @Column(nullable = false)
    private LocalTime time;    // 시간 (예: 09:00:00)

    public void setMyMedicine(MyMedicine myMedicine) {
        this.myMedicine = myMedicine;
    }
}