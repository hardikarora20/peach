package com.app.peach.user;

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
        Optional<UserEntity> user = userService.findByEmail(email);

        if (user.isPresent())
            return new ResponseEntity("User found", HttpStatus.OK);
        return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestParam String email, @RequestParam String password) {
        UserEntity user = userService.createUser(email, password);
        return ResponseEntity.ok(user);
    }
}