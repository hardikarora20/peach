package com.app.peach.message;

import com.app.peach.common.exception.BadRequestException;
import com.app.peach.common.exception.ForbiddenException;
import com.app.peach.common.exception.NotFoundException;
import com.app.peach.common.util.SecurityUtils;
import com.app.peach.match.MatchEntity;
import com.app.peach.match.MatchRepository;
import com.app.peach.message.dto.MessageResponseDTO;
import com.app.peach.message.dto.SendMessageRequestDTO;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          MatchRepository matchRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageResponseDTO sendMessage(UUID currentUserId, SendMessageRequestDTO req) {

        // 1) validate input
        if (req.getMatchId() == null) throw new BadRequestException("matchId is required");
        if (req.getContent() == null || req.getContent().trim().isEmpty())
            throw new BadRequestException("content is required");

        // 2) clean and validate content
        String content = req.getContent().trim();
        if (content.length() > 1000) throw new BadRequestException("content too long");

        // 3) load match
        MatchEntity match = matchRepository.findById(req.getMatchId())
                .orElseThrow(() -> new NotFoundException("Match not found"));

        // 4) authorization: must be participant
        UUID u1 = match.getUser1().getId();
        UUID u2 = match.getUser2().getId();
        if (!currentUserId.equals(u1) && !currentUserId.equals(u2)) {
            throw new ForbiddenException("Not allowed");
        }

        // 5) load sender
        UserEntity sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 6) save message
        //    (your constructor sets sentAt = LocalDateTime.now())
        MessageEntity saved = messageRepository.save(new MessageEntity(match, sender, content));

        // 7) update match summary fields (for frontend list)
        match.setLastMessageAt(saved.getSentAt());
        match.setLastMessagePreview(content.length() > 120 ? content.substring(0, 120) : content);

        // 8) increment unread for the OTHER user (receiver)
        match.incrementUnreadFor(currentUserId);

        // 9) persist match updates (important!)
        matchRepository.save(match);

        // 10) return response dto
        return new MessageResponseDTO(
                saved.getId(),
                match.getId(),
                currentUserId,
                saved.getContent(),
                saved.getSentAt()
        );
    }


    @Transactional
    public List<MessageResponseDTO> getMessages(UUID matchId, boolean markRead) {

        // 0) get current user id from security context
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        // 1) verify if match exists
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found"));

        // 2) authorization: user must be part of match
        UUID u1 = match.getUser1().getId();
        UUID u2 = match.getUser2().getId();
        if (!currentUserId.equals(u1) && !currentUserId.equals(u2)) {
            throw new ForbiddenException("Not allowed");
        }

        // 3) if markRead=true then reset unread for current user
        //    (NOTE: lastMessageAt / lastMessagePreview will NOT be touched here)
        if (markRead) {
            match.markReadFor(currentUserId);
            matchRepository.save(match); // persist unread reset
        }

        // 4) fetch all messages for this match (asc by sentAt)
        // 5) map MessageEntity -> MessageResponseDTO and return
        return messageRepository.findByMatch_IdOrderBySentAtAsc(matchId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private MessageResponseDTO toDTO(MessageEntity m) {
        return new MessageResponseDTO(
                m.getId(),
                m.getMatch().getId(),
                m.getSender().getId(),
                m.getContent(),
                m.getSentAt()
        );
    }
}