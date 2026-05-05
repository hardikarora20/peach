package com.app.peach.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findByMatch_IdOrderBySentAtAsc(UUID matchId);
    Optional<MessageEntity> findTopByMatch_IdOrderBySentAtDesc(UUID matchId);
    long countByMatch_IdAndSender_IdNotAndSentAtAfter(UUID matchId, UUID currentUserId, java.time.LocalDateTime after);
}
