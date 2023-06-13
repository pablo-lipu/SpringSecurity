package com.gateway.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gateway.models.User;
import com.gateway.repository.UserRepository;
import com.gateway.security.UserPrincipal;
import com.gateway.security.jwt.IJwtProvider;
import com.gateway.services.IAuthenticationService;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

	/*
	 * El AuthenticationManager una interfaz en Spring Security que proporciona
	 * funcionalidad de autenticación. Es responsable de autenticar las credenciales
	 * de un usuario, como el nombre de usuario y la contraseña.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IJwtProvider iJwtProvider;

	@Autowired
	private UserRepository userRepository;

	@Override
	public User signInAndReturnJWT(User signInRequest)// Solicitid de inicio de sesion
	{

		System.out.println("User en signInAndReturnJWT:  " + signInRequest);

		// Buscar al usuario por su correo en la DB
		User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
				() -> new UsernameNotFoundException("El usuario no fue encontrado:" + signInRequest.getEmail()));

		/*
		 * Llamar al metodo para authenticar sus credenciales del usuario authentication
		 * variable, Este objeto contiene información sobre el usuario autenticado, como
		 * su nombre de usuario, autoridades y otros detalles.
		 */
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), signInRequest.getPassword()));

		System.out.println("Objeto user authenticado:  " + authentication.toString());
		// Obtengo el usuario y sus roles
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		System.out.println("Usuario y roles:  " + authentication.getPrincipal().toString());
		// Creo el token para este usuario
		String jwt = iJwtProvider.generateToken(userPrincipal);
		System.out.println("Token para mi usuario authenticado" + jwt.toString());

		User sigInUser = userPrincipal.getUser();
		System.out.println("Obtengo solo el Usuario:  " + userPrincipal.getUser().toString());

		//Seteo el token al usuario authenticado 
		sigInUser.setToken(jwt);
		System.out.println("Usuario authenticado completo con todo y token: " + sigInUser);
		
		//Finalmente retorno el usuario authenticado completo 
		return sigInUser;
	}

}
