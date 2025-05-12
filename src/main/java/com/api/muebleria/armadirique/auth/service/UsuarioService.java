package com.api.muebleria.armadirique.auth.service;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;

import java.util.Set;

public interface UsuarioService {

    //metodo que se encarga de asignar usuarios la tbkla intermedia usuarioroles
    public Usuario guardarUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws Exception;

    //Agregmos metodo para obtener uuario
    public Usuario obtenerUsuario(String username);

    public void eliminarUsuario(long usuarioId) throws Exception;
}
