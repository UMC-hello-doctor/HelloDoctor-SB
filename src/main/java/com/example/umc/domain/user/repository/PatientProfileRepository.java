package com.example.umc.domain.user.repository;

import com.example.umc.domain.user.entity.PatientProfile;
import com.example.umc.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    // 유저 객체로 프로필 찾기
    Optional<PatientProfile> findByUser(User user);
}