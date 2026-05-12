package com.app.peach.profile.dto;

public class ProfileExistsResponseDTO {
    boolean exists;

    public ProfileExistsResponseDTO(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
