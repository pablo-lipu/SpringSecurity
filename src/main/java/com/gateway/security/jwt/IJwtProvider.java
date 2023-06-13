package com.gateway.security.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.gateway.models.User;
import com.gateway.security.UserPrincipal;

public interface IJwtProvider {

	/**
	 * Para poder generar este token , este se basa en la data del usuario
	 * 
	 * @author Pablo Rosas
	 * @return token en cadena
	 * @param Data dek usuario de mi app
	 */
	String generateToken(UserPrincipal auth);

	String generateToken(User user);

	/**
	 * Obtiene la authentication Convertir ell token plano y debe convertirlo en un
	 * objeto de tipo ususrio para saber si es correcto
	 */
	Authentication getAuthentication(HttpServletRequest request);

	/**
	 * Validar si el token es valido
	 */
	boolean isTokenValid(HttpServletRequest request);
}
