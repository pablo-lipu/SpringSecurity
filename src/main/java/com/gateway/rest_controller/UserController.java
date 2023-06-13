package com.gateway.rest_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.models.Role;
import com.gateway.models.User;
import com.gateway.repository.UserRepository;
import com.gateway.security.UserPrincipal;
import com.gateway.services.IUserService;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	private IUserService iUserService;

	/**
	 * Actualiza el rol del usuario de User a ADMIN solo funciona con token k trae un rol de ADMIN
	 */
	@PutMapping("/change/{role}")
	public ResponseEntity<?> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Role role) {

		System.out.println("Usuario Actual en sesion en mi App Backend: " + userPrincipal.toString());
		
		iUserService.changeRole(role, userPrincipal.getUsername());

		return ResponseEntity.ok(true);
	}
	
	/**
	 * Actualiza el rol del usuario de User a ADMIN
	 */
	@PutMapping("/update-role")
	public ResponseEntity<?> actualizarRol(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody User userRol) {
		
		
		System.out.println("Modelo de envio en el body: " + userRol);

		System.out.println("Usuario actual kien ara la actualizacion:  " + userPrincipal.getUsername()  + "   ***************************************");
		System.out.println("rol enviado en el requestbody: "  +userRol.getRole());
		System.out.println("rol enviado en el requestbody: "  +userRol.getId());
		
		 Role role = userRol.getRole();
		
		iUserService.updteRole( role, userRol.getId());
		
		return null;
	}
	
	/*
	 * El usuario cuando esta logueado dentro de mi app, lo k tiene el cliente es un
	 * token de seguridad ese token llega al servidor y si al comprobarlo cumple con
	 * todas las reglas y es un usuario valido de mi app. El userPrincipal
	 * representa al usuario actual k esta en sesion en mi app internamente, el
	 * token viene en el request, hay k validarlo
	 */

	/**
	 * @author Pablo Rosas
	 * 
	 *         Metodo devuelve al usuario actual authenticado y logueado solo para
	 *         administrador
	 * @param
	 * @return
	 */
	@GetMapping("/current")
	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		System.out.println("usuario actual authenticado:  " + userPrincipal.getUsername());
		return new ResponseEntity<>(iUserService.findByUsernameReturnToken(userPrincipal.getUsername()), HttpStatus.OK);
	}

	/**
	 * Devuelve una lista de usuarios solo a quien esta autenticado y logueado, pero
	 * es para usuarios en general donde simplemente este logueado envia el token
	 * con un rol ADMIN
	 */
	@GetMapping("/list")
	public List<User> listUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {

		System.out.println("usuario actual authenticado:  " + userPrincipal.getUsername());

		return userRepo.findAll();
	}
}
