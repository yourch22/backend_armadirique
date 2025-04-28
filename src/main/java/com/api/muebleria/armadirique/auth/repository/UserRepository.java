package com.api.muebleria.armadirique.auth.repository;

import java.util.Optional;

import com.api.muebleria.armadirique.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
}