ALTER TABLE profilesALTER TABLE dating_intent VARCHAR(50) NULL,
  ADD COLUMN connection_preference VARCHAR(50) NULL,
  ADD COLUMN open_to_long_distance VARCHAR(10) NULL,

  ADD COLUMN communication_style VARCHAR(50) NULL,
  ADD COLUMN love_language VARCHAR(50) NULL,
  ADD COLUMN conflict_style VARCHAR(50) NULL,

  ADD COLUMN drink_habit VARCHAR(20) NULL,
  ADD COLUMN smoke_habit VARCHAR(20) NULL,
  ADD COLUMN food_preference VARCHAR(20) NULL,
  ADD COLUMN sleep_style VARCHAR(20) NULL;

-- Multi-select tables
CREATE TABLE profile_personality_traits (
  profile_id VARCHAR(36) NOT NULL,
  trait VARCHAR(50) NOT NULL,
  PRIMARY KEY (profile_id, trait)
);

CREATE TABLE profile_core_values (
  profile_id VARCHAR(36) NOT NULL,
  value_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (profile_id, value_name)
);

CREATE TABLE profile_dealbreakers (
  profile_id VARCHAR(36) NOT NULL,
  dealbreaker VARCHAR(50) NOT NULL,
  PRIMARY KEY (profile_id, dealbreaker)
);

CREATE TABLE profile_interests (
  profile_id VARCHAR(36) NOT NULL,
  interest VARCHAR(50) NOT NULL,
  PRIMARY KEY (profile_id, interest)
);
