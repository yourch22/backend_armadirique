package com.api.muebleria.armadirique;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import com.api.muebleria.armadirique.excepcions.UsuarioFountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

// Archivo principal de la aplicación
@SpringBootApplication
public class MuebleriaArmadiriqueApplication implements CommandLineRunner {//implementacion solo para probar ingresar datos

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Autowired
	private UsuarioService usuarioService;
	public static void main(String[] args) {
		SpringApplication.run(MuebleriaArmadiriqueApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {

		try{
		Usuario usuario = new Usuario();
			usuario.setNombre("Jorge");
			usuario.setApellidos("Flores M");
			usuario.setUsername("admin");
			usuario.setPassword(bCryptPasswordEncoder.encode("12345"));
			usuario.setEmail("admin@gmail.com");
			usuario.setTelefono("988212020");
			usuario.setPerfil("perfil/perfil_1_icon_user.png");
			usuario.setDireccion("av.arequipa");
			usuario.setEstado(true);
			usuario.setCiudad("Lima");
			Rol rol = new Rol();
			rol.setRolId(1L);
			rol.setRolNombre("ADMIN");
			Set<UsuarioRol> usuariosRoles = new HashSet<>();
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRol(rol);
			usuarioRol.setUsuario(usuario);
			usuariosRoles.add(usuarioRol);
			Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario,usuariosRoles);
			System.out.println(usuarioGuardado.getUsername());
		}catch (UsuarioFountException exception){
			exception.printStackTrace();
		}
		try {
			// --- Nuevo Usuario CLIENTE ---
			Usuario usuarioCliente = new Usuario();
			usuarioCliente.setNombre("Mario"); // Nombre diferente
			usuarioCliente.setApellidos("Gomez R");
			usuarioCliente.setUsername("cliente"); // Username diferente
			usuarioCliente.setPassword(bCryptPasswordEncoder.encode("12345")); // Contraseña diferente
			usuarioCliente.setEmail("cliente@gmail.com"); // Email diferente
			usuarioCliente.setTelefono("987654321");
			usuarioCliente.setPerfil("perfil/perfil_2_icon_user.png"); // Perfil diferente (si tienes)
			usuarioCliente.setDireccion("calle del sol 456");
			usuarioCliente.setEstado(true);
			usuarioCliente.setCiudad("Cusco");

			Rol rolCliente = new Rol();
			rolCliente.setRolId(2L); // <-- ¡IMPORTANTE! Asegúrate que este ID exista en tu tabla de Roles como "CLIENT"
			rolCliente.setRolNombre("CLIENTE"); // El nombre del rol en tu base de datos

			Set<UsuarioRol> usuariosRolesCliente = new HashSet<>();
			UsuarioRol usuarioRolCliente = new UsuarioRol();
			usuarioRolCliente.setRol(rolCliente);
			usuarioRolCliente.setUsuario(usuarioCliente);
			usuariosRolesCliente.add(usuarioRolCliente);

			Usuario usuarioGuardadoCliente = usuarioService.guardarUsuario(usuarioCliente, usuariosRolesCliente);
			System.out.println("Usuario CLIENTE guardado/existente: " + usuarioGuardadoCliente.getUsername());

		} catch (UsuarioFountException exception){
			System.out.println("Error: El usuario CLIENTE ya existe. " + exception.getMessage());
			// exception.printStackTrace(); // Descomenta si necesitas el stack trace completo
		}
	}
}
