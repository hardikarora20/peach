package com.app.peach.user;

import com.app.peach.common.util.SecurityUtils;
import com.app.peach.security.JwtUtil;
import com.app.peach.user.dto.LoginRequestDTO;
import com.app.peach.user.dto.LoginResponseDTO;
import com.app.peach.user.dto.RegisterRequestDTO;
import com.app.peach.user.dto.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findUser(@RequestParam String email) {
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getCreatedAt()
                )
        );
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserResponseDTO user = userService.createUser(registerRequestDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
//        this is when we get no execptions
            UserEntity user = userService.login(loginRequestDTO);
            String token = JwtUtil.generateToken(user.getId());
            return ResponseEntity.ok(new LoginResponseDTO(user.getId(), token,"Login successful"));
        } catch (RuntimeException e) {
//        if we get exceptions that will go in catch
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null, null,"Invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO>  aboutMe(){
        UUID userId = SecurityUtils.getCurrentUserId();
        System.out.println("JWT userId: " + userId);
        if (userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserResponseDTO me = userService.getMe(userId);
//        System.out.println(me);
//        System.out.println(me.toString());
        if(me == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(me);
    }
}