CREATE TABLE photos (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  url VARCHAR(500) NOT NULL,
  position INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL
);

CREATE INDEX idx_photos_user_pos ON photos(user_id, position);