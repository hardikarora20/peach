package com.app.peach.swipe.dto;

import java.util.UUID;

public class SwipeResponseDTO {
    //    this is from the server to client if the other person has liked
    //    if it's a match when matched, match id and things
    private boolean liked;
    private boolean matchCreated;
    private UUID matchId;

    public SwipeResponseDTO(boolean liked, boolean matchCreated, UUID matchId) {
        this.liked = liked;
        this.matchCreated = matchCreated;
        this.matchId = matchId;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isMatchCreated() {
        return matchCreated;
    }

    public UUID getMatchId() {
        return matchId;
    }
}