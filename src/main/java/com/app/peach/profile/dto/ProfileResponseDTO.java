package com.app.peach.profile.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileResponseDTO {
    private UUID profileId;
    private UUID userId;
    private String name;
    private Integer age;
    private String gender;
    private String bio;
    private String location;
    private LocalDateTime updatedAt;

    public ProfileResponseDTO(UUID profileId, UUID userId, String name, Integer age, String gender, String bio, String location, LocalDateTime updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.updatedAt = updatedAt;
    }

    public UUID getProfileId() { return profileId; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}