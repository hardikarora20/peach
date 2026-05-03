package com.app.peach.swipe;

import com.app.peach.match.MatchEntity;
import com.app.peach.match.MatchRepository;
import com.app.peach.swipe.dto.SwipeRequestDTO;
import com.app.peach.swipe.dto.SwipeResponseDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public SwipeService(SwipeRepository swipeRepository, MatchRepository matchRepository, UserRepository userRepository) {
        this.swipeRepository = swipeRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SwipeResponseDTO swipe(UUID swiperId, SwipeRequestDTO req) {

        UUID targetId = req.getTargetUserId();
        boolean liked = "LIKE".equalsIgnoreCase(req.getAction());

        // 1) No self swipe (just in case)
        if (swiperId.equals(targetId)) {
            throw new RuntimeException("Cannot swipe on yourself");
        }

        // 2) Ensure target exists (verifing if both the users exist)
        UserEntity swiper = userRepository.findById(swiperId).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity target = userRepository.findById(targetId).orElseThrow(() -> new RuntimeException("Target user not found"));

        // 3) Prevent duplicates (also protected by DB unique constraint/ if somehow opens a profile and swipes again)
        if (swipeRepository.existsBySwiper_IdAndTarget_Id(swiperId, targetId)) {
            throw new RuntimeException("Already swiped");
        }

        // 4) Save swipe
        swipeRepository.save(new SwipeEntity(swiper, target, liked));

        // 5) If DISLIKE, no match possible, if the other person has already disliked then nothing can be done
        if (!liked) {
            return new SwipeResponseDTO(false, false, null);
        }

        // 6) If LIKE, check reverse like (target liked swiper)
        //  you have liked that is okay but has the other person liked you,
        //  this checks if both exists and other person has liked you
        //  in parameters swiper and target are swapped for this
        boolean reverseLiked = swipeRepository.existsBySwiper_IdAndTarget_IdAndLikedTrue(targetId, swiperId);

        //  reverse liked is if the other person has liked, if no then match not yet created, but like sent
        if (!reverseLiked) {
            return new SwipeResponseDTO(true, false, null);
        }

        // 7) Canonical pair ordering for match uniqueness
        //  just in case this was not considered and somehow someone got till this point
        //  then swapped order could be inserted, this is to avoid that
        UUID u1 = swiperId.compareTo(targetId) <= 0 ? swiperId : targetId;
        UUID u2 = swiperId.compareTo(targetId) <= 0 ? targetId : swiperId;

        //  checks if the entry for these two users already existed, if yes they already matched
        if (matchRepository.existsByUser1_IdAndUser2_Id(u1, u2)) {
            return new SwipeResponseDTO(true, true, null); // match already existed
        }

        //  then verify the users and add a match if everything looks fine till now
        UserEntity user1 = userRepository.findById(u1).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity user2 = userRepository.findById(u2).orElseThrow(() -> new RuntimeException("User not found"));

        MatchEntity match = matchRepository.save(new MatchEntity(user1, user2));

        return new SwipeResponseDTO(true, true, match.getId());
    }
}