package com.app.peach.registration;

import com.app.peach.security.JwtUtil;
import com.app.peach.user.UserEntity;
import com.app.peach.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final int EXPIRY_MIN = 10, MAX_ATTEMPTS = 5, RESEND_COOLDOWN_SEC = 60;
    private final SecureRandom random = new SecureRandom();

    private final PendingRegistrationRepository pendingRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final MailService mail;
    // JwtUtil is static – no field needed

    @Transactional
    public void requestOtp(String email, String password) {
        email = normalize(email);
        if (userRepo.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists.");
        if (password == null || password.length() < 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters.");

        PendingRegistration p = pendingRepo.findById(email).orElseGet(PendingRegistration::new);
        p.setEmail(email);
        p.setPasswordHash(encoder.encode(password));
        sendNewCode(p);
    }

    @Transactional
    public void resend(String email) {
        PendingRegistration p = pendingRepo.findById(normalize(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Start sign-up again."));
        if (p.getLastSentAt().plusSeconds(RESEND_COOLDOWN_SEC).isAfter(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Please wait a minute before requesting another code.");
        sendNewCode(p);
    }

    @Transactional
    public Map<String, String> verify(String email, String otp) {
        email = normalize(email);
        if (otp == null || otp.trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the 6-digit code.");

        PendingRegistration p = pendingRepo.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Start sign-up again."));

        if (p.getExpiresAt().isBefore(LocalDateTime.now())) {
            pendingRepo.delete(p);
            throw new ResponseStatusException(HttpStatus.GONE, "Code expired. Request a new one.");
        }
        if (p.getAttempts() >= MAX_ATTEMPTS)
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many attempts. Request a new code.");

        if (!encoder.matches(otp.trim(), p.getOtpHash())) {
            p.setAttempts(p.getAttempts() + 1);
            pendingRepo.save(p);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect code.");
        }

        // Code is correct: create the real user (same as the old POST /users)
        UserEntity u = userRepo.save(new UserEntity(email, p.getPasswordHash()));
        pendingRepo.delete(p);

        Map<String, String> res = new HashMap<>();
        res.put("token", JwtUtil.generateToken(u.getId()));   // static call
        res.put("userId", u.getId().toString());
        return res;
    }

    private String normalize(String email) {
        if (email == null || email.trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required.");
        return email.trim().toLowerCase();
    }

    private void sendNewCode(PendingRegistration p) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        p.setOtpHash(encoder.encode(code));
        p.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MIN));
        p.setAttempts(0);
        p.setLastSentAt(LocalDateTime.now());
        pendingRepo.save(p);
        mail.sendOtp(p.getEmail(), code);
    }
}