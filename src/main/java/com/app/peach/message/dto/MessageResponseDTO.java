package com.app.peach.message.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageResponseDTO {

    private UUID messageId;
    private UUID matchId;
    private UUID senderId;
    private String content;
    private LocalDateTime sentAt;

    public MessageResponseDTO(UUID messageId, UUID matchId, UUID senderId, String content, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.matchId = matchId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public UUID getMessageId() { return messageId; }
    public UUID getMatchId() { return matchId; }
    public UUID getSenderId() { return senderId; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
}
