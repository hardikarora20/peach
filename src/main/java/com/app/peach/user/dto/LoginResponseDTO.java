package com.app.peach.user.dto;

import java.util.UUID;

public class LoginResponseDTO {

    private UUID userId;
    private String message;

    public LoginResponseDTO(UUID userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}