package com.app.peach.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {
    List<PhotoEntity> findByUser_IdOrderByPositionAsc(UUID userId);
    long countByUser_Id(UUID userId);

    void deleteByUser_Id(UUID userId);
}