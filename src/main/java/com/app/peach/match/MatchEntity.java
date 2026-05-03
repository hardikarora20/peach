package com.app.peach.match;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches", uniqueConstraints = {@UniqueConstraint(columnNames = {"user1_id", "user2_id"})})
public class MatchEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private UserEntity user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private UserEntity user2;

    @Column(nullable = false)
    private LocalDateTime matchedAt;

    protected MatchEntity() {}

    public MatchEntity(UserEntity user1, UserEntity user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.matchedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UserEntity getUser1() { return user1; }
    public UserEntity getUser2() { return user2; }
    public LocalDateTime getMatchedAt() { return matchedAt; }
}