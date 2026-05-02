package com.app.peach.user;

import com.app.peach.user.dto.LoginRequestDTO;
import com.app.peach.user.dto.LoginResponseDTO;
import com.app.peach.user.dto.RegisterRequestDTO;
import com.app.peach.user.dto.UserResponseDTO;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(RegisterRequestDTO registerRequestDTO) {
        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
        UserEntity user = new UserEntity(registerRequestDTO.getEmail(), hashedPassword);
        return toResponseDTO(userRepository.save(user));
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public UserEntity login(LoginRequestDTO loginRequestDTO) {
        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (user == null)
            throw new RuntimeException("Invalid credentials");
        boolean match = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash());
        if(!match)
            throw new RuntimeException("Invalid credentials");
        return user;
    }

    private UserResponseDTO toResponseDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}