package com.api.muebleria.armadirique.auth.entity;

import org.springframework.security.core.GrantedAuthority;

//implementa metodo GrandAurhoriti
public class Authority implements GrantedAuthority {

    private String authority;
    public Authority(String authority) {
        this.authority = authority;
    }
    @Override
    public String getAuthority() {
        return this.authority;
    }
}
