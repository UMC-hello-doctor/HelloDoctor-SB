package com.example.umc.domain.medicine.repository;

import com.example.umc.domain.medicine.entity.MyMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyMedicineRepository extends JpaRepository<MyMedicine, Long> {
}