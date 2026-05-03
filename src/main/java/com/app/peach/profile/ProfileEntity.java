package com.app.peach.profile;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(length = 500)
    private String bio;

    private String location;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected ProfileEntity() {}

    public ProfileEntity(UserEntity user, String name, Integer age, String gender, String bio, String location) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, Integer age, String gender, String bio, String location) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UserEntity getUser() { return user; }
    public String getName() { return name; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}