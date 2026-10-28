package com.app.peach.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/users/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;

    @PostMapping("/request-otp")
    public Map<String, String> request(@RequestBody Map<String, String> b) {
        service.requestOtp(b.get("email"), b.get("password"));
        return Collections.singletonMap("message", "Code sent");
    }

    @PostMapping("/resend")
    public Map<String, String> resend(@RequestBody Map<String, String> b) {
        service.resend(b.get("email"));
        return Collections.singletonMap("message", "Code sent");
    }

    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody Map<String, String> b) {
        return service.verify(b.get("email"), b.get("otp"));
    }
}