package com.api.muebleria.armadirique;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

// Archivo principal de la aplicación
@SpringBootApplication
public class MuebleriaArmadiriqueApplication  {//implementacion solo para probar ingresar datos



	public static void main(String[] args) {
		SpringApplication.run(MuebleriaArmadiriqueApplication.class, args);
	}


		/*try{
		Usuario usuario = new Usuario();

			usuario.setNombre("Jorge");
			usuario.setApellido("Flores");
			usuario.setUsername("jorge");
			usuario.setPassword("12345");
			usuario.setEmail("jorge@gmail.com");
			usuario.setTelefono("988212020");
			usuario.setPerfil("foto.png");
			usuario.setDireccion("av.arequipa");
			usuario.setEstado(true);
			usuario.setCiudad("Lima");


			Rol rol = new Rol();
			rol.setRolId(2L);
			rol.setRolNombre("ADMIN");

			Set<UsuarioRol> usuariosRoles = new HashSet<>();
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRol(rol);
			usuarioRol.setUsuario(usuario);
			usuariosRoles.add(usuarioRol);

			Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario,usuariosRoles);
			System.out.println(usuarioGuardado.getUsername());
	/*	}catch (UsuarioFoundException exception){
			exception.printStackTrace();
		}*/

}
