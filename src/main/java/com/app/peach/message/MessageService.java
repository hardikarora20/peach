package com.app.peach.message;

import com.app.peach.common.exception.BadRequestException;
import com.app.peach.common.exception.ForbiddenException;
import com.app.peach.common.exception.NotFoundException;
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

        // 1) Validate input
        if (req.getMatchId() == null) throw new BadRequestException("matchId is required");
        if (req.getContent() == null || req.getContent().trim().isEmpty())
            throw new BadRequestException("content is required");

        //  if no data then return exc if not then clean and take it in content
        String content = req.getContent().trim();
        if (content.length() > 1000) throw new BadRequestException("content too long");

        // 2) Load match
        //  get match info now, which will contain the id of the 2 users and the match id when it was created
        MatchEntity match = matchRepository.findById(req.getMatchId())
                .orElseThrow(() -> new NotFoundException("Match not found"));

        // 3) Authorization: must be participant
        //  get userIds of both the users and check if one of the id matches the logged in id
        UUID u1 = match.getUser1().getId();
        UUID u2 = match.getUser2().getId();
        System.out.println(currentUserId);
        System.out.println(u1 + "  " + u2);
        System.out.println(!currentUserId.equals(u2));
        System.out.println(!currentUserId.equals(u1));
        if (!currentUserId.equals(u1) && !currentUserId.equals(u2)) {
            throw new ForbiddenException("Not allowed");
        }

        //  if everything looks good, load the currentUser then
        // 4) Load sender
        UserEntity sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 5) Save message
        //  and then save the actual message
        MessageEntity saved = messageRepository.save(new MessageEntity(match, sender, content));

        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessages(UUID currentUserId, UUID matchId) {

        //  to start with verifying if match exists
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found"));

        //  then the same thing matching user ids and stuff
        UUID u1 = match.getUser1().getId();
        UUID u2 = match.getUser2().getId();
        if (!currentUserId.equals(u1) && !currentUserId.equals(u2)) {
            throw new ForbiddenException("Not allowed");
        }

        //  then getting all messages for this particular match id in asc order
        //  as we get the response in message entity so loading it in messageresponsedto then
        //  returning the list of message responses
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