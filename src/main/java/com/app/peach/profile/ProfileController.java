package com.app.peach.profile;

import com.app.peach.common.util.SecurityUtils;
import com.app.peach.profile.dto.*;
import com.app.peach.userLocation.dto.FeedRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:5173"})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDTO> getMyProfile() {
        //      get the logged in user id
        UUID userId = SecurityUtils.getCurrentUserId();
        //      if got nothing that means no user is logged in
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //      or else get the profile info if present return that else not found
        ProfileResponseDTO profile = profileService.getMyProfile(userId);
        if (profile == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponseDTO> upsertMyProfile(@RequestBody ProfileUpsertRequestDTO req) {
        //     getting the user id yet again from the authorization token jwt
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //     if userid not found or not matching that means not allowed or unauthorized
        //     if user id is present then this method updates or creates new profile
        ProfileResponseDTO profile = profileService.upsertMyProfile(userId, req);
        if (profile == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PublicProfileDTO> getPublicProfile(
            @PathVariable UUID userId) {

        UUID requesterId = SecurityUtils.getCurrentUserId();
        if (requesterId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        PublicProfileDTO profile =
                profileService.getPublicProfile(requesterId, userId);

        if (profile == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(profile);
    }

//    @GetMapping("/feed")
//    public ResponseEntity<List<PublicProfileDTO>> getFeed() {
//
//        UUID userId = SecurityUtils.getCurrentUserId();
//        if (userId == null)
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        return ResponseEntity.ok(profileService.getFeed(userId));
//    }

    @PostMapping("/feed")
    public ResponseEntity<List<PublicProfileDTO>> getFeed(@RequestBody FeedRequestDTO request, @RequestParam(defaultValue = "50") int limit
    , @RequestParam(required = false, defaultValue = "50") Double range) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(profileService.getFeed(request));
    }

    @GetMapping("/me/exists")
    public ResponseEntity<ProfileExistsResponseDTO> myProfileExists() {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).build();

        boolean exists = profileService.isMyCoreProfileComplete(userId);
        return ResponseEntity.ok(new ProfileExistsResponseDTO(exists));
    }

    @GetMapping("/getId/{userId}")
    public ResponseEntity<ProfileIdResponse> getProfileId(@PathVariable UUID userId) {
        UUID profileId = profileService.getProfileIdByUserId(userId);
        return ResponseEntity.ok(new ProfileIdResponse(profileId));
    }
}