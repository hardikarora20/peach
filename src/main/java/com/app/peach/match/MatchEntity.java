package com.app.peach.match;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.Type;import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
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

    private LocalDateTime lastMessageAt;

    @Column(length = 500)
    private String lastMessagePreview;

    @Column(nullable = false)
    private int unreadCountUser1 = 0;

    @Column(nullable = false)
    private int unreadCountUser2 = 0;


    public void incrementUnreadFor(UUID senderId) {
        if (user1.getId().equals(senderId)) {
            unreadCountUser2++;
        } else if (user2.getId().equals(senderId)) {
            unreadCountUser1++;
        } else {
            throw new IllegalArgumentException("Sender is not part of this match");
        }
    }



    public void markReadFor(UUID userId) {
        if (user1.getId().equals(userId)) {
            unreadCountUser1 = 0;
        } else if (user2.getId().equals(userId)) {
            unreadCountUser2 = 0;
        } else {
            throw new IllegalArgumentException("User is not part of this match");
        }
    }


    public int getUnreadCountFor(UUID userId) {
        if (user1.getId().equals(userId)) {
            return unreadCountUser1;
        }
        if (user2.getId().equals(userId)) {
            return unreadCountUser2;
        }
        return 0;
    }

    public UUID getOtherUserId(UUID userId) {
        if (user1.getId().equals(userId)) return user2.getId();
        return user1.getId();
    }
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

    // ✅ getters for new fields
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public String getLastMessagePreview() { return lastMessagePreview; }

    // ✅ setters for service updates
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public void setLastMessagePreview(String lastMessagePreview) { this.lastMessagePreview = lastMessagePreview; }

}


