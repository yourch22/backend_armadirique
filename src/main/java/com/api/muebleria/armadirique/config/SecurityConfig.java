package com.api.muebleria.armadirique.config;

import com.api.muebleria.armadirique.auth.security.JwtAuthenticationEntryPoint;
import com.api.muebleria.armadirique.auth.security.JwtAuthenticationFilter;
import com.api.muebleria.armadirique.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // O usa BCryptPasswordEncoder si prefieres más seguridad encripta contraseña
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/*",      // Rutas para login/registro
                                "/usuarios/",   // Rutas para usuarios (¿registro? asegúrate que no exponga datos sensibles)
                                "/v3/api-docs/**", // Documentación OpenAPI/Swagger
                                "/swagger-ui/**",  // UI de Swagger
                                "/swagger-ui.html",// HTML de Swagger UI
                                // ¡LA CLAVE! Permitir acceso a los recursos estáticos (imágenes, etc.)
                                "/uploads/**" // <--- AÑADIDO ESTO
                        ).permitAll() /* se agrega correcto rutas con buenas practicas*/
                        // Permitir acceso a todas las operaciones GET para /productos
                        .requestMatchers(HttpMethod.GET, "/productos").permitAll() // <--- ¡AÑADIDO ESTO!
                        // Si tienes paginación para /productos, también deberías permitirla
                        .requestMatchers(HttpMethod.GET, "/productos/pagina/**").permitAll() // Por ejemplo, si tu paginación usa /productos/pagina/0
                        // Si tienes el endpoint de categorías de productos en /productos/categorias
                        .requestMatchers(HttpMethod.GET, "/productos/categorias").permitAll() // Para el mrtodo getTypes()
                        // Permitir solicitudes OPTIONS para pre-vuelo de CORS (si es necesario y no está cubierto por cors.disable())
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permite OPTIONS para cualquier ruta
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}