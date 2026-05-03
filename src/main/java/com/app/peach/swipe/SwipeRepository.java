package com.app.peach.swipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SwipeRepository extends JpaRepository<SwipeEntity, UUID> {

    boolean existsBySwiper_IdAndTarget_Id(UUID swiperId, UUID targetId);

    boolean existsBySwiper_IdAndTarget_IdAndLikedTrue(UUID swiperId, UUID targetId);
}