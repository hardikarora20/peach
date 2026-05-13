package com.app.peach.match;

import com.app.peach.match.dto.MatchItemDTO;
import com.app.peach.message.MessageRepository;
import com.app.peach.photo.PhotoRepository;
import com.app.peach.profile.ProfileEntity;
import com.app.peach.profile.ProfileRepository;
import com.app.peach.profile.dto.ProfilePromptDTO;
import com.app.peach.profile.dto.ProfileResponseDTO;
import com.app.peach.profile.dto.ProfileUpsertRequestDTO;
import com.app.peach.profile.dto.PublicProfileDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ProfileRepository profileRepository;
    private final MessageRepository messageRepository;
    private final PhotoRepository  photoRepository;
    private final ObjectMapper objectMapper;

    public MatchService(MatchRepository matchRepository, ProfileRepository profileRepository, UserRepository userRepository, MessageRepository messageRepository, PhotoRepository photoRepository, ObjectMapper objectMapper) {
        this.matchRepository = matchRepository;
        this.profileRepository = profileRepository;
        this.messageRepository = messageRepository;
        this.photoRepository = photoRepository;
        this.objectMapper = objectMapper;
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
    private ProfilePromptDTO fromPromptJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) return null;
            return objectMapper.readValue(json, ProfilePromptDTO.class);
        } catch (Exception e) {
            // if DB has bad data, just ignore
            return null;
        }
    }
    public List<MatchItemDTO> getMyMatches(UUID currentUserId) {
        List<MatchEntity> matches = matchRepository.findByUser1_IdOrUser2_Id(currentUserId, currentUserId);
        List <MatchItemDTO> listOfMatches = new ArrayList<>();
        for(MatchEntity currMatch: matches){
            UUID idOfMatch, idOfOtherUser;
            if(currMatch.getUser1().getId().equals(currentUserId)){
                idOfMatch = currMatch.getUser2().getId();
                idOfOtherUser = currMatch.getUser1().getId();
            }
            else{
                idOfOtherUser = currMatch.getUser2().getId();
                idOfMatch = currMatch.getUser1().getId();
            }
            ProfileEntity profileOfMatch = profileRepository.findByUser_Id(idOfMatch).get();
//            ProfileEntity profileOfOtherMatch = profileRepository.findByUserId(idOfOtherUser);
            System.out.println("profileOfMatch: " + profileOfMatch.getId());
//            System.out.println("profileOfOtherMatch: " + profileOfOtherMatch);
            List<String> images = loadImageUrls(profileOfMatch.getUserId());

            List<ProfilePromptDTO> prompts = readPrompts(profileOfMatch);
            PublicProfileDTO publicProfileOfMatch = profileOfMatch == null
                    ? new PublicProfileDTO(idOfMatch, idOfOtherUser, null, null, null, null, null, null, null, null, null, null, null, null,  null, null, null, null, null, null, null, null, null, null)
                    : new PublicProfileDTO(idOfMatch, idOfOtherUser,
//                    profileOfMatch.getId(),
                    profileOfMatch.getName(),
                    profileOfMatch.getAge(),
                    profileOfMatch.getGender(),
                    profileOfMatch.getBio(),
                    profileOfMatch.getLocation(),
                    profileOfMatch.getDatingIntent(),
                    profileOfMatch.getConnectionPreference(),
                    profileOfMatch.getOpenToLongDistance(),
                    profileOfMatch.getPersonalityTraits(),
                    profileOfMatch.getCommunicationStyle(),
                    profileOfMatch.getLoveLanguage(),
                    profileOfMatch.getConflictStyle(),
                    profileOfMatch.getDrinkHabit(),
                    profileOfMatch.getSmokeHabit(),
                    profileOfMatch.getFoodPreference(),
                    profileOfMatch.getSleepStyle(),
                    profileOfMatch.getCoreValues(),
                    profileOfMatch.getDealbreakers(),
                    profileOfMatch.getInterests(),
                    images,
                    profileOfMatch.getOpeningLine(),
                    prompts);
            listOfMatches.add(new MatchItemDTO(currMatch.getId(), currMatch.getMatchedAt(), publicProfileOfMatch));
        }
        return listOfMatches;
    }
    private List<String> loadImageUrls(UUID userId) {
        return photoRepository.findByUser_IdOrderByPositionAsc(userId)
                .stream()
                .map(p -> p.getUrl())
                .collect(Collectors.toList());
    }

}