package com.gateway.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.models.User;
import com.gateway.services.IAuthenticationService;
import com.gateway.services.IUserService;

@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {

	@Autowired
	private IAuthenticationService iAuthenticationService;

	@Autowired
	private IUserService iUserService;

	@PostMapping("sign-up")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		System.out.println("usuario para registrar: ");
		System.out.println(user.toString());

		// Si este usuario ya esta registrado priviamente n mi DB.
		if (iUserService.findByUsername(user.getUsername()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
        //Si el correo ya esta registrado previamente n la DB.
		if (iUserService.findByEmail(user.getEmail()).isPresent()) {
			System.out.println(" *************  ¡el usuario ya existe!  ********************");
			return new ResponseEntity<>("Ya existe un usuario con ese nombre de usuario.", HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(iUserService.saveUser(user), HttpStatus.CREATED);
	}

	@PostMapping("sign-in")
	public ResponseEntity<?> signIn(@RequestBody User user) {
		System.out.println("Usuario resibido en la request para inicio de sesion: " + user);
		// llamo al servicio de Authenticacion
		// loguea el usuario y escribele un token
		return new ResponseEntity<>(iAuthenticationService.signInAndReturnJWT(user), HttpStatus.OK);
	}

}
