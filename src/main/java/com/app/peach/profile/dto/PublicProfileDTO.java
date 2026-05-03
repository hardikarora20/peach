package com.app.peach.profile.dto;

import java.util.UUID;

public class PublicProfileDTO {

    private UUID userId;
    private String name;
    private Integer age;
    private String gender;
    private String bio;
    private String location;

    public PublicProfileDTO(
            UUID userId,
            String name,
            Integer age,
            String gender,
            String bio,
            String location
    ) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }
}