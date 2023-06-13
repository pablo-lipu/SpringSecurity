package com.gateway.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * -Convierte el rol que esta almacenado en la base de datos al formato del
 * contexto de spring security como un authority -Ayuda a recupera el jwt del
 * request
 * 
 * @author Pablo Rosas
 */
public class SecurityUtils {

	/**
	 * cada authority de spring debe comenzar con el prefijo de ROLE Todos los roles
	 * k yo valla a crear y trabajar dentro de mi modelo de ususrios deben de tener
	 * simepre por delante la palabra ROLE_
	 */
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String AUTH_HEADER = "authorization"; // nomeclatura k me envia el cliente por defecto
	public static final String AUTH_TOKEN_TYPE = "Bearer";
	/**
	 * esta es la nomenclaura con la cual los navegadores deben enviar el token al
	 * servidor dentro de la seccion del header, existe un authorization y dentro de
	 * ese voy agregar el texto bearer espacio
	 */
	public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " "; // Tipo de token
	// Metodo para concatener los datos

	public static SimpleGrantedAuthority convertToAuthority(String role) {
		// Dar formato al role k entra como parametro, este role viene de la db
		String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
		return new SimpleGrantedAuthority(formattedRole);
	}

	/**
	 * Metodo para extaer el token desde el request
	 * @author Pablo Rosas
	 * @param el objeto request que envia el cliente
	 * @return jwt si existe tal token
	 */
	public static String extractAuthTokenFromRequest(HttpServletRequest request) {
		/* Extraer el token del header */
		String bearerToken = request.getHeader(AUTH_HEADER);
		/*
		 * Validar si si existe el token en la requets si el cliente me lo esta enviando
		 */
		/*
		 * Valido si existe el bearerToken dentro del header del cliente y si si existe
		 * ese token debe empezar con la palabra Bearar mas un espacio
		 */
		if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;
	}

}
