package com.example.umc.domain.user.repository;
import com.example.umc.domain.user.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AllergyRepository extends JpaRepository<Allergy, Long> { }