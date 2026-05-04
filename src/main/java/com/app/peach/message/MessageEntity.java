package com.app.peach.message;


import com.app.peach.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.app.peach.match.MatchEntity;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    protected MessageEntity() {}

    public MessageEntity(MatchEntity match, UserEntity sender, String content) {
        this.match = match;
        this.sender = sender;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public MatchEntity getMatch() { return match; }
    public UserEntity getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
}

