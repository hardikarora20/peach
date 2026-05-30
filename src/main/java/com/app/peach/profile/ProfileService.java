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
import com.app.peach.userLocation.UserLocationEntity;
import com.app.peach.userLocation.UserLocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.peach.common.util.SecurityUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.app.peach.userLocation.dto.FeedRequestDTO;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final ObjectMapper objectMapper;
    private final UserLocationRepository userLocationRepository;

    public ProfileService(ProfileRepository profileRepository,
                          UserRepository userRepository,
                          PhotoRepository photoRepository,
                          ObjectMapper objectMapper,
                          UserLocationRepository userLocationRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.objectMapper = objectMapper;
        this.userLocationRepository = userLocationRepository;
    }


    public ProfileResponseDTO getMyProfile(UUID userId) {
        Optional<ProfileEntity> profile = profileRepository.findByUser_Id(userId);
        if(profile.isPresent())
            return toDTO(profile.orElse(null));
        return null;
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
Optional<ProfileEntity> profileOptional = profileRepository.findByUser_Id(userId);
System.out.println(profileOptional);

ProfileEntity profileEntity;

if (!profileOptional.isPresent()) {
    profileEntity = new ProfileEntity(
        user,
        req.getName(),
        req.getAge(),
        req.getGender(),
        req.getBio(),
        req.getLocation(),
        req.getxCoordinate(),
        req.getyCoordinate()
    );
} else {
    profileEntity = profileOptional.get();
    profileEntity.updateCore(
        req.getName(),
        req.getAge(),
        req.getGender(),
        req.getBio(),
        req.getLocation(),
        req.getxCoordinate(),
        req.getyCoordinate()
    );
}
        profileEntity.updateQuestions(
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

        profileEntity.setOpeningLine(req.getOpeningLine());
        writePrompts(profileEntity, req.getProfilePrompts());

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
        ProfileEntity saved = profileRepository.save(profileEntity);

        return toDTO(saved);
    }

//    public List<PublicProfileDTO> getFeed(UUID userId, int limit, Double range) {
//
//
//        ProfileEntity myProfile = profileRepository.findByUser_Id(userId).get();
//
//        if (myProfile == null) {
//            throw new RuntimeException("Current user profile not found");
//        }
//
//        if (myProfile.getxCoordinate() == null || myProfile.getyCoordinate() == null) {
//            throw new RuntimeException("Current user coordinates not set");
//        }
//
//        double allowedRange = range != null ? range : 50.0;
//
//        int safeLimit = Math.min(Math.max(limit, 1), 200); // cap to prevent abuse
//        Pageable pageable = PageRequest.of(0, safeLimit);
//
//        List<ProfileEntity> profiles = profileRepository.findFeedForUser(userId, pageable);
//
//        return profiles.stream()
//                .filter(profile -> profile.getxCoordinate() != null && profile.getyCoordinate() != null)
//                .map(profile -> {
//                    double distance = calculateDistance(
//                            myProfile.getxCoordinate(),
//                            myProfile.getyCoordinate(),
//                            profile.getxCoordinate(),
//                            profile.getyCoordinate()
//                    );
//
//                    PublicProfileDTO dto = toPublicDTO(profile);
//                    dto.setDistance(distance);
//
//                    return dto;
//                })
//                .filter(dto -> dto.getDistance() <= allowedRange)
//                .sorted(Comparator.comparing(PublicProfileDTO::getDistance))
//                .collect(Collectors.toList());
//    }

    public List<PublicProfileDTO> getFeed(FeedRequestDTO request) {

        UUID currentUserId = SecurityUtils.getCurrentUserId();

        System.out.println(request);

        // validate request
        if (request.getXCoordinate() == null || request.getYCoordinate() == null) {
            throw new RuntimeException("Location is required");
        }

        double ax = request.getXCoordinate();
        double ay = request.getYCoordinate();
        double range = request.getRange() != null ? request.getRange() : 50.0;
        System.out.println(ax+" "+ay+" "+range);
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(currentUser);
        // ✅ update A's location (fresh)
        upsertLocation(currentUser, ax, ay);

        System.out.println("updated");
        // ✅ get others (B)
        List<ProfileEntity> others =
                profileRepository.findFeedForUser(currentUserId);
        System.out.println(others);
        List<PublicProfileDTO> result = new ArrayList<>();

        for(ProfileEntity currUser: others){
            UserLocationEntity other = userLocationRepository.findByUser_Id(currUser.getUser().getId());
            // skip if location missing
            if (other.getXCoordinate() == null || other.getYCoordinate() == null) {
                continue;
            }

            // ✅ optional: skip stale data
//            if (other.getUpdatedAt() == null ||
//                    other.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(30))) {
//                continue;
//            }

            double distance = calculateDistance(
                    ax, ay,
                    other.getXCoordinate(),
                    other.getYCoordinate()
            );
            System.out.println(distance);
            if (distance > range) {
                continue;
            }

            UUID otherUserId = other.getUser().getId();

            ProfileEntity profile = profileRepository.findByUser_Id(otherUserId).get();

            if (profile == null) {
                continue;
            }

            PublicProfileDTO dto = toPublicDTO(profile);
            dto.setDistance(distance);

            result.add(dto);
        }

        result.sort(Comparator.comparing(PublicProfileDTO::getDistance));
        System.out.println(result);
        return result;
    }

    private void upsertLocation(UserEntity user, Double x, Double y) {

        try {
            UserLocationEntity location =
                    userLocationRepository.findByUser_Id(user.getId());

            if (location == null) {
                location = new UserLocationEntity();
                location.setUser(user);
            }

            location.setXCoordinate(x);
            location.setYCoordinate(y);
            location.setUpdatedAt(LocalDateTime.now());

            userLocationRepository.save(location);

        } catch (DataIntegrityViolationException ex) {

            // ✅ Another request inserted first → fetch and update
            UserLocationEntity existing =
                    userLocationRepository.findByUser_Id(user.getId());

            existing.setXCoordinate(x);
            existing.setYCoordinate(y);
            existing.setUpdatedAt(LocalDateTime.now());

            userLocationRepository.save(existing);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371; // Earth radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distance in km
    }

    private ProfileResponseDTO toDTO(ProfileEntity p) {
        List<String> images = loadImageUrls(p.getUserId());

        List<ProfilePromptDTO> prompts = readPrompts(p);

        return new ProfileResponseDTO(
                p.getId(),
                p.getUserId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation(),
                p.getxCoordinate(),
                p.getyCoordinate(),
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
        System.out.println(profile);
        if (profile == null) return null;

        return toPublicDTO(profile);
    }

    private PublicProfileDTO toPublicDTO(ProfileEntity p) {
        List<String> images = loadImageUrls(p.getUserId());
        List<ProfilePromptDTO> prompts = readPrompts(p);
        return new PublicProfileDTO(
                p.getId(),
                p.getUserId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getBio(),
                p.getLocation(),
                p.getxCoordinate(),
                p.getyCoordinate(),
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

//    public List<PublicProfileDTO> getFeed(UUID userId) {
//        List <ProfileEntity> list = profileRepository.findByUser_IdNot(userId);
//        List <PublicProfileDTO> result = new ArrayList<>();
//        for(ProfileEntity curr: list){
//            result.add(toPublicDTO(curr));
//        }
//        return result;
//    }

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