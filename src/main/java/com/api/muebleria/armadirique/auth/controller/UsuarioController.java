package com.api.muebleria.armadirique.auth.controller;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")//dependencia para permitir acceso al controlador
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;//inyectamos contrasela encriptada

    //metodo ara bguardar usuario rol
    @PostMapping("/")
    public Usuario GuardarUsuario(@RequestBody Usuario usuario) throws Exception {
        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
        Set<UsuarioRol> usuarioroles = new HashSet<>();

        Rol rol = new Rol();
        rol.setRolId(2L);
        rol.setRolNombre("CLIENTE");

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

       // usuarioRoles.add(usuarioRol);
        usuarioroles.add(usuarioRol);
        return usuarioService.guardarUsuario(usuario, usuarioroles);
    }
    //metodfo para iobtener usuario
    @GetMapping("/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username) throws Exception {
        return usuarioService.obtenerUsuario(username);
    }
    //metodo para eliminar usuario por id
    @DeleteMapping("/{usuarioId}")
    public void eliminarUsuario(@PathVariable("usuarioId") Long usuarioId) throws Exception {
        usuarioService.eliminarUsuario(usuarioId);
    }
}
