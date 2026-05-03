package com.app.peach.swipe.dto;

import java.util.UUID;

public class SwipeRequestDTO {
    //    client to server, a liked/disliked b
    private UUID targetUserId;
    private String action; // "LIKE" or "DISLIKE"

    public UUID getTargetUserId() {
        return targetUserId;
    }

    public String getAction() {
        return action;
    }
}