package com.app.peach.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {


    ProfileEntity findByUserId(UUID userId);
    Optional<ProfileEntity> findById(UUID id);
    List<ProfileEntity> findByUserIdNot(UUID userId);


    @Query("select p.id from ProfileEntity p where p.user.id = :userId")
    Optional<UUID> findProfileIdByUserId(@Param("userId") UUID userId);

    @Query("select case when count(p) > 0 then true else false end from ProfileEntity p where p.user.id = :userId and p.user is not null and p.name is not null and length(trim(p.name)) > 0 and p.age is not null and p.gender is not null and length(trim(p.gender)) > 0 and p.bio is not null and length(trim(p.bio)) > 0 and p.location is not null and length(trim(p.location)) > 0 and p.updatedAt is not null")
    boolean existsCompleteCoreProfile(@Param("userId") UUID userId);

}