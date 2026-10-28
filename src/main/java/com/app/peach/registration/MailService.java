package com.app.peach.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private static final int EXPIRY_MINUTES = 10;

    @Value("${brevo.api-key:}")   private String apiKey;
    @Value("${mail.from:}")       private String from;
    @Value("${mail.from-name:Peach}") private String fromName;

    private final RestTemplate rest = new RestTemplate();
    private String template; // loaded once

    public void sendOtp(String to, String code) {
        // Local dev: no key → print the code instead of emailing it
        if (apiKey.isEmpty()) {
            log.info("[DEV] OTP for {}: {}", to, code);
            return;
        }

        String html = loadTemplate()
                .replace("{{CODE}}", code)
                .replace("{{MINUTES}}", String.valueOf(EXPIRY_MINUTES))
                .replace("{{EMAIL}}", HtmlUtils.htmlEscape(to))
                .replace("{{YEAR}}", String.valueOf(Year.now().getValue()));

        String text = "Your Peach verification code is " + code + "\n\n"
                + "It expires in " + EXPIRY_MINUTES + " minutes. Never share it with anyone.\n"
                + "Didn't try to sign up? You can ignore this email.\n\n— Peach";

        Map<String, Object> sender = new HashMap<>();
        sender.put("name", fromName);
        sender.put("email", from);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", Collections.singletonList(Collections.singletonMap("email", to)));
        body.put("subject", code + " is your Peach code");
        body.put("htmlContent", html);
        body.put("textContent", text);
        body.put("tags", Collections.singletonList("otp"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("api-key", apiKey);

        try {
            rest.postForEntity(BREVO_URL, new HttpEntity<>(body, headers), String.class);
        } catch (RestClientException e) {
            log.error("Brevo send failed for {}: {}", to, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "We couldn't send the code right now. Please try again in a minute.");
        }
    }

    private String loadTemplate() {
        if (template == null) {
            try {
                template = StreamUtils.copyToString(
                        new ClassPathResource("templates/otp-email.html").getInputStream(),
                        StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Missing templates/otp-email.html", e);
            }
        }
        return template;
    }
}