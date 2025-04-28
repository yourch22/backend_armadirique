package com.api.muebleria.armadirique;

import com.api.muebleria.armadirique.auth.entity.Role;
import com.api.muebleria.armadirique.auth.entity.User;
import com.api.muebleria.armadirique.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

// Archivo principal de la aplicación
@SpringBootApplication
public class MuebleriaArmadiriqueApplication {
	public static void main(String[] args) {
		SpringApplication.run(MuebleriaArmadiriqueApplication.class, args);
	}
	//al iniciar la aplicacion creamos un usuario por defecto
	@Bean
	public CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = User.builder()
						.username("admin@gmail.com")
						.password(passwordEncoder.encode("admin123")) // contraseña segura
						.firstname("Super")
						.lastname("Admin")
						.country("Global")
						.role(Role.ADMIN)
						.build();
				userRepository.save(admin);
				System.out.println("✅ Usuario administrador creado por defecto: admin@gmail.com / admin123");
			} else {
				System.out.println("ℹ️ El usuario administrador ya existe.");
			}
		};
	}
}
