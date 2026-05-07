package com.app.peach.profile.dto;

import java.util.List;
import java.util.Set;

public class ProfileUpsertRequestDTO {

    // Core Profile (required)
    private String name;
    private Integer age;
    private String gender;
    private String location;
    private String bio;

    // 💖 Intent & Dating (single choice)
    private String datingIntent;          // e.g. "SERIOUS_RELATIONSHIP"
    private String connectionPreference;  // e.g. "LONG_TERM"
    private String openToLongDistance;    // e.g. "YES" / "NO" / "MAYBE"

    // 🧬 Personality & Compatibility
    private Set<String> personalityTraits; // multi: ["INTROVERT", "FUNNY"]
    private String communicationStyle;     // e.g. "TEXT_A_LOT"
    private String loveLanguage;           // e.g. "QUALITY_TIME"
    private String conflictStyle;          // e.g. "TALK_IT_OUT_IMMEDIATELY"

    // 🌿 Lifestyle
    private String drinkHabit;             // "YES" / "OCCASIONALLY" / "NO"
    private String smokeHabit;             // "YES" / "OCCASIONALLY" / "NO"
    private String foodPreference;         // "VEG" / "NON_VEG" / "EGGETARIAN" / "VEGAN"
    private String sleepStyle;             // "EARLY_BIRD" / "NIGHT_OWL" / "DEPENDS"

    // 🎯 Values & Preferences
    private Set<String> coreValues;        // multi
    private Set<String> dealbreakers;      // multi

    // 🎨 Interests
    private Set<String> interests;         // multi

    private String openingLine;
    private List<ProfilePromptDTO> profilePrompts;

    public String getOpeningLine() { return openingLine; }
    public void setOpeningLine(String openingLine) { this.openingLine = openingLine; }

    public List<ProfilePromptDTO> getProfilePrompts() { return profilePrompts; }
    public void setProfilePrompts(List<ProfilePromptDTO> profilePrompts) { this.profilePrompts = profilePrompts; }

    public ProfileUpsertRequestDTO() {}

    // ------------------ Getters & Setters ------------------

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getDatingIntent() { return datingIntent; }
    public void setDatingIntent(String datingIntent) { this.datingIntent = datingIntent; }

    public String getConnectionPreference() { return connectionPreference; }
    public void setConnectionPreference(String connectionPreference) { this.connectionPreference = connectionPreference; }

    public String getOpenToLongDistance() { return openToLongDistance; }
    public void setOpenToLongDistance(String openToLongDistance) { this.openToLongDistance = openToLongDistance; }

    public Set<String> getPersonalityTraits() { return personalityTraits; }
    public void setPersonalityTraits(Set<String> personalityTraits) { this.personalityTraits = personalityTraits; }

    public String getCommunicationStyle() { return communicationStyle; }
    public void setCommunicationStyle(String communicationStyle) { this.communicationStyle = communicationStyle; }

    public String getLoveLanguage() { return loveLanguage; }
    public void setLoveLanguage(String loveLanguage) { this.loveLanguage = loveLanguage; }

    public String getConflictStyle() { return conflictStyle; }
    public void setConflictStyle(String conflictStyle) { this.conflictStyle = conflictStyle; }

    public String getDrinkHabit() { return drinkHabit; }
    public void setDrinkHabit(String drinkHabit) { this.drinkHabit = drinkHabit; }

    public String getSmokeHabit() { return smokeHabit; }
    public void setSmokeHabit(String smokeHabit) { this.smokeHabit = smokeHabit; }

    public String getFoodPreference() { return foodPreference; }
    public void setFoodPreference(String foodPreference) { this.foodPreference = foodPreference; }

    public String getSleepStyle() { return sleepStyle; }
    public void setSleepStyle(String sleepStyle) { this.sleepStyle = sleepStyle; }

    public Set<String> getCoreValues() { return coreValues; }
    public void setCoreValues(Set<String> coreValues) { this.coreValues = coreValues; }

    public Set<String> getDealbreakers() { return dealbreakers; }
    public void setDealbreakers(Set<String> dealbreakers) { this.dealbreakers = dealbreakers; }

    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }
}