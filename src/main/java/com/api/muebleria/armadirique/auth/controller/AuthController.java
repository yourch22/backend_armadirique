package com.api.muebleria.armadirique.auth.controller;

import com.api.muebleria.armadirique.auth.dto.AuthResponse;
import com.api.muebleria.armadirique.auth.service.AuthService;
import com.api.muebleria.armadirique.auth.dto.LoginRequest;
import com.api.muebleria.armadirique.auth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login") // 👈 agregué "/"
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register") // 👈 agregué "/"
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
