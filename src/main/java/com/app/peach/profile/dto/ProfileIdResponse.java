package com.app.peach.profile.dto;

import java.util.UUID;

public class ProfileIdResponse {
    UUID profileId;

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public ProfileIdResponse(UUID profileId) {
        this.profileId = profileId;
    }
}
