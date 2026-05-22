package com.app.peach.userLocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserLocationRepository extends JpaRepository<UserLocationEntity, UUID> {

    UserLocationEntity findByUser_Id(UUID userId);

    List<UserLocationEntity> findByUser_IdNot(UUID userId);
}