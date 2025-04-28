package com.api.muebleria.armadirique.modules.client.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {
    @PreAuthorize("hasAuthority('CLIENT')") // 👈 Solo CLIENT puede acceder
    @PostMapping(value = "client")
    public String welcome()
    {
        return "Iniciaste Sesion como Cliente";
    }
}