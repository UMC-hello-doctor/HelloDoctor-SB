package com.example.umc.domain.user.repository;

import com.example.umc.domain.user.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    boolean existsByUserId(Long userId);
    Optional<PatientProfile> findByUserId(Long userId);
}
