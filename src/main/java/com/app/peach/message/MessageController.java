package com.app.peach.message;

import com.app.peach.common.util.SecurityUtils;
import com.app.peach.message.dto.MessageResponseDTO;
import com.app.peach.message.dto.SendMessageRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> send(@RequestBody SendMessageRequestDTO req) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(messageService.sendMessage(userId, req));
    }

    //  this is when you open a chat, getting all messages from this particular match
    @GetMapping("/{matchId}")
    public ResponseEntity<List<MessageResponseDTO>> get(@PathVariable UUID matchId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(messageService.getMessages(userId, matchId));
    }

//    @GetMapping("/{matchId}")
//    public List<MessageResponseDTO> getMessages(@RequestAttribute("currentUserId") UUID currentUserId,
//                                                @PathVariable UUID matchId,
//                                                @RequestParam(defaultValue = "false") boolean markRead) {
//        return messageService.getMessages(currentUserId, matchId, markRead);
//    }
}