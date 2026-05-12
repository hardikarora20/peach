package com.app.peach.profile;

import com.app.peach.common.exception.BadRequestException;
import com.app.peach.common.exception.ProfileNotFoundException;
import com.app.peach.photo.PhotoEntity;
import com.app.peach.photo.PhotoRepository;
import com.app.peach.profile.dto.ProfilePromptDTO;
import com.app.peach.profile.dto.ProfileResponseDTO;
import com.app.peach.profile.dto.ProfileUpsertRequestDTO;
import com.app.peach.profile.dto.PublicProfileDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final ObjectMapper objectMapper;

    public ProfileService(ProfileRepository profileRepository,
                          UserRepository userRepository,
                          PhotoRepository photoRepository,
                          ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.objectMapper = objectMapper;
    }


    public ProfileResponseDTO getMyProfile(UUID userId) {
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile == null)
            return null;
        return toDTO(profile);
    }


    public UUID getProfileIdByUserId(UUID userId) {
        return profileRepository.findProfileIdByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for userId=" + userId));
    }


    //    update or insert profile
    //    deleteByUser_Id(...) triggers a delete operation internally.
    //    Hibernate requires a transaction for delete/remove.
    //    Since your service method isn’t in a transaction (or Spring proxy isn’t applying it), there’s no active EntityManager transaction → boom.
    @Transactional
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
            profile.updateCore(req.getName(), req.getAge(), req.getGender(), req.getBio(), req.getLocation());
        }

        profile.updateQuestions(
                req.getDatingIntent(),
                req.getConnectionPreference(),
                req.getOpenToLongDistance(),
                req.getPersonalityTraits(),
                req.getCommunicationStyle(),
                req.getLoveLanguage(),
                req.getConflictStyle(),
                req.getDrinkHabit(),
                req.getSmokeHabit(),
                req.getFoodPreference(),
                req.getSleepStyle(),
                req.getCoreValues(),
                req.getDealbreakers(),
                req.getInterests()
        );

        profile.setOpeningLine(req.getOpeningLine());
        writePrompts(profile, req.getProfilePrompts());

        int currCount = 0;
        System.out.println(photoRepository.countByUser_Id(userId));
        photoRepository.deleteByUser_Id(userId);
        System.out.println("after delete: " + photoRepository.countByUser_Id(userId));
        List <String> imageList = req.getImages();
        for(String currImage: imageList){
            photoRepository.save(new PhotoEntity(user, currImage, currCount));
            currCount++;
        }

        System.out.println("insert: " + photoRepository.countByUser_Id(userId));
//        save images in photo

        //    saving the newly created/updated profile
        ProfileEntity saved = profileRepository.save(profile);

        return toDTO(saved);
    }

    private ProfileResponseDTO toDTO(ProfileEntity p) {
        List<String> images = loadImageUrls(p.getUser().getId());

        List<ProfilePromptDTO> prompts = readPrompts(p);

        return new ProfileResponseDTO(
                p.getId(),
                p.getUser().getId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation(),
                p.getUpdatedAt(),
        p.getDatingIntent(),
        p.getConnectionPreference(),
        p.getOpenToLongDistance(),
        p.getPersonalityTraits(),
        p.getCommunicationStyle(),
        p.getLoveLanguage(),
                p.getConflictStyle(),
        p.getDrinkHabit(),
        p.getSmokeHabit(),
        p.getFoodPreference(),
        p.getSleepStyle(),
        p.getCoreValues(),
        p.getDealbreakers(),
        p.getInterests(),
        images,
                p.getOpeningLine(),
                prompts

        );
    }

    public PublicProfileDTO getPublicProfile(UUID requesterId, UUID profileUserId) {

        // prevent viewing self via public endpoint
        if (requesterId.equals(profileUserId)) {
            return null;
        }

        Optional<ProfileEntity> optProfile = profileRepository.findById(profileUserId);
        ProfileEntity profile = optProfile.get();
        if (profile == null) return null;

        return toPublicDTO(profile);
    }

    private PublicProfileDTO toPublicDTO(ProfileEntity p) {
        List<String> images = loadImageUrls(p.getUser().getId());
        List<ProfilePromptDTO> prompts = readPrompts(p);
        return new PublicProfileDTO(
                p.getId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation(),
                p.getDatingIntent(),
                p.getConnectionPreference(),
                p.getOpenToLongDistance(),
                p.getPersonalityTraits(),
                p.getCommunicationStyle(),
                p.getLoveLanguage(),
                p.getConflictStyle(),
                p.getDrinkHabit(),
                p.getSmokeHabit(),
                p.getFoodPreference(),
                p.getSleepStyle(),
                p.getCoreValues(),
                p.getDealbreakers(),
                p.getInterests(),
                images,
                p.getOpeningLine(),
                prompts

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

    private List<String> loadImageUrls(UUID userId) {
        return photoRepository.findByUser_IdOrderByPositionAsc(userId)
                .stream()
                .map(p -> p.getUrl())
                .collect(Collectors.toList());
    }

    private String toPromptJson(ProfilePromptDTO p) {
        try {
            if (p == null) return null;
            return objectMapper.writeValueAsString(p);
        } catch (Exception e) {
            // keep it simple with your current exception approach
            throw new BadRequestException("Invalid prompt format");
        }
    }

    private ProfilePromptDTO fromPromptJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) return null;
            return objectMapper.readValue(json, ProfilePromptDTO.class);
        } catch (Exception e) {
            // if DB has bad data, just ignore
            return null;
        }
    }

    private List<ProfilePromptDTO> readPrompts(ProfileEntity p) {
        List<ProfilePromptDTO> list = new ArrayList<>();
        ProfilePromptDTO a = fromPromptJson(p.getPrompt1());
        ProfilePromptDTO b = fromPromptJson(p.getPrompt2());
        ProfilePromptDTO c = fromPromptJson(p.getPrompt3());
        if (a != null) list.add(a);
        if (b != null) list.add(b);
        if (c != null) list.add(c);
        return list;
    }

    private void writePrompts(ProfileEntity p, List<ProfilePromptDTO> prompts) {
        List<ProfilePromptDTO> safe = prompts == null ? Collections.emptyList() : prompts;

        // max 3
        ProfilePromptDTO p1 = safe.size() > 0 ? safe.get(0) : null;
        ProfilePromptDTO p2 = safe.size() > 1 ? safe.get(1) : null;
        ProfilePromptDTO p3 = safe.size() > 2 ? safe.get(2) : null;

        p.setPrompt1(p1 == null ? null : toPromptJson(p1));
        p.setPrompt2(p2 == null ? null : toPromptJson(p2));
        p.setPrompt3(p3 == null ? null : toPromptJson(p3));
    }

    public boolean isMyCoreProfileComplete(UUID userId) {
        return profileRepository.existsCompleteCoreProfile(userId);
    }

}