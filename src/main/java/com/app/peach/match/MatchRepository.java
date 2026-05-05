package com.app.peach.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {

    boolean existsByUser1_IdAndUser2_Id(UUID user1Id, UUID user2Id);
    List<MatchEntity> findByUser1_IdOrUser2_Id(UUID userId1, UUID userId2);

    @Query("select m from MatchEntity m where m.user1.id = :userId or m.user2.id = :userId order by m.lastMessageAt desc nulls last, m.matchedAt desc")
    List<MatchEntity> findAllForUserSorted(@Param("userId") UUID userId);
}