package com.app.peach.photo;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
public class PhotoEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PhotoEntity() {}

    public PhotoEntity(UserEntity user, String url, Integer position) {
        this.user = user;
        this.url = url;
        this.position = position == null ? 0 : position;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UserEntity getUser() { return user; }
    public String getUrl() { return url; }
    public Integer getPosition() { return position; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setPosition(Integer position) { this.position = position; }
}