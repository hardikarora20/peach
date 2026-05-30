package com.app.peach.profile;

import java.util.Set;
import com.app.peach.user.UserEntity;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;

@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // Core
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(length = 500)
    private String bio;

    private Double xCoordinate;
    private Double yCoordinate;

    private String location;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 💖 Intent & Dating (single choice)
    @Column(name = "dating_intent", length = 50)
    private String datingIntent;

    @Column(name = "connection_preference", length = 50)
    private String connectionPreference;

    @Column(name = "open_to_long_distance", length = 10)
    private String openToLongDistance;

    // 🧬 Personality & Compatibility
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "profile_personality_traits", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "trait", length = 50)
    private Set<String> personalityTraits = new HashSet<>();

    @Column(name = "communication_style", length = 50)
    private String communicationStyle;

    @Column(name = "love_language", length = 50)
    private String loveLanguage;

    @Column(name = "conflict_style", length = 50)
    private String conflictStyle;

    // 🌿 Lifestyle
    @Column(name = "drink_habit", length = 20)
    private String drinkHabit;

    @Column(name = "smoke_habit", length = 20)
    private String smokeHabit;

    @Column(name = "food_preference", length = 20)
    private String foodPreference;

    @Column(name = "sleep_style", length = 20)
    private String sleepStyle;

    // 🎯 Values & Preferences (multi)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "profile_core_values", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "value_name", length = 50)
    private Set<String> coreValues = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "profile_dealbreakers", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "dealbreaker", length = 50)
    private Set<String> dealbreakers = new HashSet<>();

    // 🎨 Interests (multi)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "profile_interests", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "interest", length = 50)
    private Set<String> interests = new HashSet<>();


    @Column(name = "prompt1", columnDefinition = "text")
    private String prompt1;

    @Column(name = "prompt2", columnDefinition = "text")
    private String prompt2;

    @Column(name = "prompt3", columnDefinition = "text")
    private String prompt3;

    @Column(name = "opening_line", length = 255)
    private String openingLine;

    public ProfileEntity() {}

    public ProfileEntity(UserEntity user) {
        this.user = user;
    }

    public ProfileEntity(UserEntity user, String name, Integer age, String gender, String bio, String location, Double xCoordinate, Double yCoordinate) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCore(String name, Integer age, String gender, String bio, String location, Double xCoordinate, Double yCoordinate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateQuestions(String datingIntent,
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

    // getters
    public UUID getId() { return id; }
    public UserEntity getUser() {   return user; }
    public UUID getUserId(){ return user.getId();}
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


    public String getPrompt1() { return prompt1; }
    public String getPrompt2() { return prompt2; }
    public String getPrompt3() { return prompt3; }
    public String getOpeningLine() { return openingLine; }

    public void setPrompt1(String prompt1) { this.prompt1 = prompt1; }
    public void setPrompt2(String prompt2) { this.prompt2 = prompt2; }
    public void setPrompt3(String prompt3) { this.prompt3 = prompt3; }
    public void setOpeningLine(String openingLine) { this.openingLine = openingLine; }

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString() {
        return "ProfileEntity{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bio='" + bio + '\'' +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", location='" + location + '\'' +
                ", updatedAt=" + updatedAt +
                ", datingIntent='" + datingIntent + '\'' +
                ", connectionPreference='" + connectionPreference + '\'' +
                ", openToLongDistance='" + openToLongDistance + '\'' +
                ", personalityTraits=" + personalityTraits +
                ", communicationStyle='" + communicationStyle + '\'' +
                ", loveLanguage='" + loveLanguage + '\'' +
                ", conflictStyle='" + conflictStyle + '\'' +
                ", drinkHabit='" + drinkHabit + '\'' +
                ", smokeHabit='" + smokeHabit + '\'' +
                ", foodPreference='" + foodPreference + '\'' +
                ", sleepStyle='" + sleepStyle + '\'' +
                ", coreValues=" + coreValues +
                ", dealbreakers=" + dealbreakers +
                ", interests=" + interests +
                ", prompt1='" + prompt1 + '\'' +
                ", prompt2='" + prompt2 + '\'' +
                ", prompt3='" + prompt3 + '\'' +
                ", openingLine='" + openingLine + '\'' +
                '}';
    }
}

