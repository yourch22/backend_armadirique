package com.api.muebleria.armadirique.auth.service.impl;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.repository.RolRepository;
import com.api.muebleria.armadirique.auth.repository.UsuarioRepository;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import com.api.muebleria.armadirique.excepcions.UsuarioFountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws Exception {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioFountException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws Exception {
        return usuarioRepository.save(usuario);
    }

    //metodo que implemeta al la interface UusarioService
    @Override
    public Usuario guardarUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws Exception {
        //indicamos que busque usuario
        Usuario usuarioLocal = usuarioRepository.findByUsername(usuario.getUsername());
        //obtnemos el usuario para validar si ya existe, para no duplicar usuarios
        if (usuarioLocal != null) {
            System.out.println("Usuario ya Existe");
            throw new UsuarioFountException("Usuario ya esta presente");
        }
        //si usuario no existe guardamos en base datos
        else {
            //guardamos roles
            for (UsuarioRol usuarioRol : usuarioRoles) {
                rolRepository.save(usuarioRol.getRol());
            }
            //obtenemso roles y los asirgnamos a usuarios
            usuario.getUsuarioRoles().addAll(usuarioRoles);
            usuarioLocal = usuarioRepository.save(usuario);
        }
        return usuarioLocal;
    }

    @Override
    public Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public void eliminarUsuario(long usuarioId) throws Exception {
        usuarioRepository.deleteById(usuarioId);
    }
}
