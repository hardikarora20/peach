package com.app.peach.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {

    ProfileEntity findByUserId(UUID userId);
    List<ProfileEntity> findByUserIdNot(UUID userId);
}