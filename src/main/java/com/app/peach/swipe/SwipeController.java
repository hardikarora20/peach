package com.app.peach.swipe;

import com.app.peach.common.util.SecurityUtils;
import com.app.peach.swipe.dto.SwipeRequestDTO;
import com.app.peach.swipe.dto.SwipeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }

    @PostMapping
    public ResponseEntity<SwipeResponseDTO> swipe(@RequestBody SwipeRequestDTO req) {
        UUID swiperId = SecurityUtils.getCurrentUserId();
        SwipeResponseDTO res = swipeService.swipe(swiperId, req);
        return ResponseEntity.ok(res);
    }
}