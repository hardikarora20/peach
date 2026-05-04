package com.app.peach.message.dto;

import java.util.UUID;

public class SendMessageRequestDTO {

    private UUID matchId;
    private String content;

    public UUID getMatchId() {
        return matchId;
    }

    public String getContent() {
        return content;
    }
}