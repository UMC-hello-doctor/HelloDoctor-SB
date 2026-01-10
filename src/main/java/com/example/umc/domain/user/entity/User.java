package com.example.umc.domain.user.entity;

import com.example.umc.domain.user.enums.Language;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class) //creatd_at 자동관리
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    //OIDC 방식
    @Column(nullable = false)
    private String providerId;

    @Column(length = 512)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Language language;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}