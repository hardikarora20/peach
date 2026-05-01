package com.app.peach.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("common")
public class HealthController {
    @GetMapping
    public ResponseEntity <String> check(){
        return ResponseEntity.ok().body("working");
    }
}