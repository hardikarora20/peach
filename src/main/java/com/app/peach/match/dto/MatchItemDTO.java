package com.app.peach.match.dto;

import com.app.peach.profile.dto.PublicProfileDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class MatchItemDTO {

    private UUID matchId;
    private LocalDateTime matchedAt;
    private PublicProfileDTO otherUser;
    private LocalDateTime lastMessageAt;
    private String lastMessagePreview;
    private int unreadCount;


    public UUID getMatchId() {
        return matchId;
    }

    public LocalDateTime getMatchedAt() {
        return matchedAt;
    }

    public PublicProfileDTO getOtherUser() {
        return otherUser;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public String getLastMessagePreview() {
        return lastMessagePreview;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public MatchItemDTO(UUID matchId, LocalDateTime matchedAt, PublicProfileDTO otherUser) {
        this.matchId = matchId;
        this.matchedAt = matchedAt;
        this.otherUser = otherUser;
    }

    public MatchItemDTO(UUID matchId, LocalDateTime matchedAt, PublicProfileDTO otherUser, LocalDateTime lastMessageAt, String lastMessagePreview, int unreadCount) {
        this.matchId = matchId;
        this.matchedAt = matchedAt;
        this.otherUser = otherUser;
        this.lastMessageAt = lastMessageAt;
        this.lastMessagePreview = lastMessagePreview;
        this.unreadCount = unreadCount;
    }
}
