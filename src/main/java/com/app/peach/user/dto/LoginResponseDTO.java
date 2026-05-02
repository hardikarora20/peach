package com.app.peach.user.dto;

import java.util.UUID;

public class LoginResponseDTO {

    private UUID userId;
    private String token;
    private String message;

    public LoginResponseDTO(UUID userId, String token, String message) {
        this.userId = userId;
        this.token = token;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}