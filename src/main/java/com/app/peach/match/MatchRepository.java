package com.app.peach.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {

    boolean existsByUser1_IdAndUser2_Id(UUID user1Id, UUID user2Id);
    List<MatchEntity> findByUser1_IdOrUser2_Id(UUID userId1, UUID userId2);
}