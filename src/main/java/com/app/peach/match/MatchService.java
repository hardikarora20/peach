package com.app.peach.match;

import com.app.peach.match.dto.MatchItemDTO;
import com.app.peach.profile.ProfileEntity;
import com.app.peach.profile.ProfileRepository;
import com.app.peach.profile.dto.ProfileResponseDTO;
import com.app.peach.profile.dto.ProfileUpsertRequestDTO;
import com.app.peach.profile.dto.PublicProfileDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ProfileRepository profileRepository;

    public MatchService(MatchRepository matchRepository, ProfileRepository profileRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.profileRepository = profileRepository;
    }

    public List<MatchItemDTO> getMyMatches(UUID currentUserId) {
        List<MatchEntity> matches = matchRepository.findByUser1_IdOrUser2_Id(currentUserId, currentUserId);
        List <MatchItemDTO> listOfMatches = new ArrayList<>();
        for(MatchEntity currMatch: matches){
            UUID idOfMatch = currMatch.getUser1().getId().equals(currentUserId) ? currMatch.getUser2().getId() : currMatch.getUser1().getId();
            ProfileEntity profileOfMatch = profileRepository.findByUserId(idOfMatch);
            PublicProfileDTO publicProfileOfMatch = profileOfMatch == null
                    ? new PublicProfileDTO(idOfMatch, null, null, null, null, null)
                    : new PublicProfileDTO(idOfMatch, profileOfMatch.getName(), profileOfMatch.getAge(), profileOfMatch.getGender(), profileOfMatch.getBio(), profileOfMatch.getLocation());
            listOfMatches.add(new MatchItemDTO(currMatch.getId(), currMatch.getMatchedAt(), publicProfileOfMatch));
        }
        return listOfMatches;
    }

}