package com.example.umc.domain.user.repository;
import com.example.umc.domain.user.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicationRepository extends JpaRepository<Medication, Long> { }