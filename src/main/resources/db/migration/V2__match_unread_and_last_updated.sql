ALTER TABLE matchesALTERETIME NULL,
  ADD COLUMN last_message_preview VARCHAR(255) NULL,
  ADD COLUMN user1_last_read_at DATETIME NULL,
  ADD COLUMN user2_last_read_at DATETIME NULL;

CREATE INDEX idx_matches_last_message_at ON matches(last_message_at);

-- Helps unread count queries and message fetch
CREATE INDEX idx_messages_match_sent_at ON messages(match_id, sent_at);
