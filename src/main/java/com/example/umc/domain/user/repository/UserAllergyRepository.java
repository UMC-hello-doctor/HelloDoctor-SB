package com.example.umc.domain.user.repository;

import com.example.umc.domain.user.entity.UserAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
}