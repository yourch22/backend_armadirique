package com.api.muebleria.armadirique.auth.controller;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //metodo ara bguardar usuario rol
    @PostMapping("/")
    public Usuario GuardarUsuario(@RequestBody Usuario usuario) throws Exception {
        Set<UsuarioRol> roles = new HashSet<>();

        Rol rol = new Rol();
        rol.setRolId(2L);
        rol.setRolNombre("NORMAL");

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

       // usuarioRoles.add(usuarioRol);
        return usuarioService.guardarUsuario(usuario, roles);
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
