package com.api.muebleria.armadirique.auth.dto;

import com.api.muebleria.armadirique.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String username;
    String password;
    String firstname;
    String lastname;

    // Nuevos campos para CLIENTES:
    private String address;
    private String phone;
    private String country;
}