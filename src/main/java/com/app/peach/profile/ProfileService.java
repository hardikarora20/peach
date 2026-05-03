package com.app.peach.profile;

import com.app.peach.profile.dto.ProfileResponseDTO;
import com.app.peach.profile.dto.ProfileUpsertRequestDTO;
import com.app.peach.profile.dto.PublicProfileDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileResponseDTO getMyProfile(UUID userId) {
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile == null)
            return null;
        return toDTO(profile);
    }

    //    update or insert profile
    public ProfileResponseDTO upsertMyProfile(UUID userId, ProfileUpsertRequestDTO req) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        //    if user is not there then we cannot proceed further
        if (user == null)
            return null;
        //    if profile is present then we get that first
        ProfileEntity profile = profileRepository.findByUserId(userId);
        //    if not then we create it from start
        if (profile == null) {
            profile = new ProfileEntity(user, req.getName(), req.getAge(), req.getGender(), req.getBio(), req.getLocation());
        } else {
        //    if already exists then we get update things of the profile not the user
            profile.update(req.getName(), req.getAge(), req.getGender(), req.getBio(), req.getLocation());
        }
        //    saving the newly created/updated profile
        ProfileEntity saved = profileRepository.save(profile);
        return toDTO(saved);
    }

    private ProfileResponseDTO toDTO(ProfileEntity p) {
        return new ProfileResponseDTO(
                p.getId(),
                p.getUser().getId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation(),
                p.getUpdatedAt()
        );
    }

    public PublicProfileDTO getPublicProfile(UUID requesterId, UUID profileUserId) {

        // prevent viewing self via public endpoint
        if (requesterId.equals(profileUserId)) {
            return null;
        }

        ProfileEntity profile = profileRepository.findByUserId(profileUserId);
        if (profile == null) return null;

        return toPublicDTO(profile);
    }

    private PublicProfileDTO toPublicDTO(ProfileEntity p) {
        return new PublicProfileDTO(
                p.getUser().getId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation()
        );
    }

    public List<PublicProfileDTO> getFeed(UUID userId) {
        List <ProfileEntity> list = profileRepository.findByUserIdNot(userId);
        List <PublicProfileDTO> result = new ArrayList<>();
        for(ProfileEntity curr: list){
            result.add(toPublicDTO(curr));
        }
        return result;
    }
}