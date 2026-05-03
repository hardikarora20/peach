package com.app.peach.match.dto;

import com.app.peach.profile.dto.PublicProfileDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class MatchItemDTO {

    private UUID matchId;
    private LocalDateTime matchedAt;
    private PublicProfileDTO otherUser;

    public MatchItemDTO(UUID matchId, LocalDateTime matchedAt, PublicProfileDTO otherUser) {
        this.matchId = matchId;
        this.matchedAt = matchedAt;
        this.otherUser = otherUser;
    }

    public UUID getMatchId() { return matchId; }
    public LocalDateTime getMatchedAt() { return matchedAt; }
    public PublicProfileDTO getOtherUser() { return otherUser; }
}
