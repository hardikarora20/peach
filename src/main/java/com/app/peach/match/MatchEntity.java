package com.app.peach.match;

import com.app.peach.user.UserEntity;
import org.hibernate.annotations.Type;import org.hibernate.annotations.GenericGenerator;

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

    // ✅ NEW: last updated / preview
    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "last_message_preview", length = 255)
    private String lastMessagePreview;

    // ✅ NEW: read pointers (per user)
    @Column(name = "user1_last_read_at")
    private LocalDateTime user1LastReadAt;

    @Column(name = "user2_last_read_at")
    private LocalDateTime user2LastReadAt;

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
    public LocalDateTime getUser1LastReadAt() { return user1LastReadAt; }
    public LocalDateTime getUser2LastReadAt() { return user2LastReadAt; }

    // ✅ setters for service updates
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public void setLastMessagePreview(String lastMessagePreview) { this.lastMessagePreview = lastMessagePreview; }

    public void setUser1LastReadAt(LocalDateTime user1LastReadAt) { this.user1LastReadAt = user1LastReadAt; }
    public void setUser2LastReadAt(LocalDateTime user2LastReadAt) { this.user2LastReadAt = user2LastReadAt; }
}

