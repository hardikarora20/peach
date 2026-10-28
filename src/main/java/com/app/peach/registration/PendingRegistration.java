package com.app.peach.registration;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;          // was org.springframework.data.annotation.Id
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_registrations")
@Getter
@Setter
public class PendingRegistration {

    @Id
    @Column(length = 255)
    private String email;              // one pending sign-up per email

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private LocalDateTime lastSentAt;
}