package com.api.muebleria.armadirique.auth.controller;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import com.api.muebleria.armadirique.modules.carrito.entity.Carrito;
import com.api.muebleria.armadirique.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.ArrayList;
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

    @Value("${app.upload.base-dir}")
    private String baseUploadDir;

    private final String PERFIL_SUBFOLDER = "perfil"; // Define subfolder for categories
    @Autowired
    private FileUploadUtil fileUploadUtil;

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

        usuarioroles.add(usuarioRol);

        // Crear carrito vacío y asignarlo al usuario
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setItems(new ArrayList<>()); // opcional si items es lazy

        usuario.setCarrito(carrito);

        return usuarioService.guardarUsuario(usuario, usuarioroles);
    }

    @PostMapping(value = "/{id}/subir-imagen",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Usuario subirImagenPerfil(@PathVariable("id") Long usuarioId,
                                     @RequestParam("imagen") MultipartFile imagen) throws Exception {

        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        // Crear carpeta si no existe
       String carpetaUploads = "uploads/perfil/";
        File carpeta = new File(carpetaUploads);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Nombre único para la imagen
        //String filename = FileUploadUtil.saveFile(baseUploadDir, PERFIL_SUBFOLDER, imagen);
        // Llama al método NO ESTÁTICO del bean inyectado:
        String filename = fileUploadUtil.saveFile(baseUploadDir, PERFIL_SUBFOLDER, imagen);

        // Guardar ruta en base de datos
        usuario.setPerfil(filename.toString());
        //return usuarioService.actualizarUsuario(usuario);
        return usuarioService.actualizarUsuario(usuario);
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
