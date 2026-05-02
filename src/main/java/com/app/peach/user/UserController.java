package com.app.peach.user;

import com.app.peach.user.dto.LoginRequestDTO;
import com.app.peach.user.dto.LoginResponseDTO;
import com.app.peach.user.dto.RegisterRequestDTO;
import com.app.peach.user.dto.UserResponseDTO;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserEntity> findUser(@RequestParam String email) {
        UserEntity user = userService.findByEmail(email);

        if (user == null)
            return new ResponseEntity("User found", HttpStatus.OK);
        return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
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
            return ResponseEntity.ok(new LoginResponseDTO(user.getId(), "Login successful"));
        } catch (RuntimeException e) {
//        if we get exceptions that will go in catch
            return ResponseEntity.badRequest().body(new LoginResponseDTO(null, "Invalid credentials"));
        }
}

}