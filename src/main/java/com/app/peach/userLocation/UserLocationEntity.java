package com.app.peach.userLocation;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_locations")
public class UserLocationEntity {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    private Double xCoordinate;
    private Double yCoordinate;

    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public UserLocationEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserLocationEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public Double getXCoordinate() {
        return xCoordinate;
    }

    public UserLocationEntity setXCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
        return this;
    }

    public Double getYCoordinate() {
        return yCoordinate;
    }

    public UserLocationEntity setYCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserLocationEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}