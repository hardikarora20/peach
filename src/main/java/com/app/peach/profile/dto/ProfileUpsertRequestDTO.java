package com.app.peach.profile.dto;

public class ProfileUpsertRequestDTO {
    private String name;
    private Integer age;
    private String gender;
    private String bio;
    private String location;

    public String getName() { return name; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
}