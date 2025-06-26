package com.api.muebleria.armadirique.auth.entity;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.api.muebleria.armadirique.modules.carrito.entity.Carrito;



import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellidos;

    @Column(length = 100)
    private String direccion;

    @Column(length = 50)
    private String ciudad;

    @Column(length = 50)
    private String provincia;

    @Column(length = 50)
    private String distrito;

    @Column(length = 50)
    private String codigopostal;

    @Column(length = 20)
    private String telefono;

    @Column(length = 20)
    private String email;

    private boolean estado = true;

    private String perfil;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Schema(hidden = true) // <--- ESTA LÍNEA
    private Carrito carrito;


    //relacion de tabla de muchos a muchos
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "usuario")
    @JsonIgnore
    @Schema(hidden = true)
    private Set<UsuarioRol> usuarioRoles = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(hidden = true)
    private Set<Producto> productos = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //creamos conjunto de autorizaciones
        Set<Authority> authorities = new HashSet<>();
        //con el foreach recorremos usuario roles y obtioene nombre de roles
        this.usuarioRoles.forEach(usuarioRol -> {
            authorities.add(new Authority(usuarioRol.getRol().getRolNombre()));
        });
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
