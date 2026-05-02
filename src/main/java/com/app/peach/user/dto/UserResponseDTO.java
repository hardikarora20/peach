package com.app.peach.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String email;
    private LocalDateTime createdAt;

    public UserResponseDTO(UUID id, String email, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}