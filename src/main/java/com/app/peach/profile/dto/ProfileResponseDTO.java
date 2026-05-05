package com.app.peach.profile.dto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    private String datingIntent;
    private String connectionPreference;
    private String openToLongDistance;
    private Set<String> personalityTraits = new HashSet<>();
    private String communicationStyle;
    private String loveLanguage;
    private String conflictStyle;
    private String drinkHabit;
    private String smokeHabit;
    private String foodPreference;
    private String sleepStyle;
    private Set<String> coreValues = new HashSet<>();
    private Set<String> dealbreakers = new HashSet<>();
    private Set<String> interests = new HashSet<>();

    public ProfileResponseDTO(UUID profileId, UUID userId, String name, Integer age, String gender, String bio, String location, LocalDateTime updatedAt, String datingIntent,
                              String connectionPreference,
                              String openToLongDistance,
                              Set<String> personalityTraits,
                              String communicationStyle,
                              String loveLanguage,
                              String conflictStyle,
                              String drinkHabit,
                              String smokeHabit,
                              String foodPreference,
                              String sleepStyle,
                              Set<String> coreValues,
                              Set<String> dealbreakers,
                              Set<String> interests) {
        this.profileId = profileId;
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.updatedAt = updatedAt;
        this.datingIntent = datingIntent;
        this.connectionPreference = connectionPreference;
        this.openToLongDistance = openToLongDistance;

        this.personalityTraits.clear();
        if (personalityTraits != null) this.personalityTraits.addAll(personalityTraits);

        this.communicationStyle = communicationStyle;
        this.loveLanguage = loveLanguage;
        this.conflictStyle = conflictStyle;

        this.drinkHabit = drinkHabit;
        this.smokeHabit = smokeHabit;
        this.foodPreference = foodPreference;
        this.sleepStyle = sleepStyle;

        this.coreValues.clear();
        if (coreValues != null) this.coreValues.addAll(coreValues);

        this.dealbreakers.clear();
        if (dealbreakers != null) this.dealbreakers.addAll(dealbreakers);

        this.interests.clear();
        if (interests != null) this.interests.addAll(interests);

        this.updatedAt = LocalDateTime.now();
    }

    public UUID getProfileId() { return profileId; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getDatingIntent() { return datingIntent; }
    public String getConnectionPreference() { return connectionPreference; }
    public String getOpenToLongDistance() { return openToLongDistance; }

    public Set<String> getPersonalityTraits() { return personalityTraits; }
    public String getCommunicationStyle() { return communicationStyle; }
    public String getLoveLanguage() { return loveLanguage; }
    public String getConflictStyle() { return conflictStyle; }

    public String getDrinkHabit() { return drinkHabit; }
    public String getSmokeHabit() { return smokeHabit; }
    public String getFoodPreference() { return foodPreference; }
    public String getSleepStyle() { return sleepStyle; }

    public Set<String> getCoreValues() { return coreValues; }
    public Set<String> getDealbreakers() { return dealbreakers; }
    public Set<String> getInterests() { return interests; }

}