package com.api.muebleria.armadirique.auth.controller;

import com.api.muebleria.armadirique.auth.dto.AuthResponse;
import com.api.muebleria.armadirique.auth.service.AuthService;
import com.api.muebleria.armadirique.auth.dto.LoginRequest;
import com.api.muebleria.armadirique.auth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

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
