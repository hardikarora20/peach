package com.app.peach.profile;

import com.app.peach.common.util.SecurityUtils;
import com.app.peach.profile.dto.ProfileResponseDTO;
import com.app.peach.profile.dto.ProfileUpsertRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
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
}