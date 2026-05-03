package com.app.peach.swipe;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "swipes", uniqueConstraints = {@UniqueConstraint(columnNames = {"swiper_id", "target_id"})})
public class SwipeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swiper_id", nullable = false)
    private UserEntity swiper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private UserEntity target;

    @Column(nullable = false)
    private boolean liked;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected SwipeEntity() {}

    public SwipeEntity(UserEntity swiper, UserEntity target, boolean liked) {
        this.swiper = swiper;
        this.target = target;
        this.liked = liked;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UserEntity getSwiper() { return swiper; }
    public UserEntity getTarget() { return target; }
    public boolean isLiked() { return liked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}