package com.gateway.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.gateway.models.User;
import com.gateway.security.UserPrincipal;
import com.gateway.utils.SecurityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProviderImpl implements IJwtProvider {

	@Value("${app.jwt.secret}")
	private String JWT_SECRET;

	@Value("${app.jwt.expiration-in-ms}")
	private Long JWT_EXPIRATION_IN_MS;

	/**
	 * Para poder generar este token , este se basa en la data del usuario
	 * 
	 * @author Pablo Rosas
	 * @return token en cadena
	 * @param Data dek usuario de mi app
	 */

	@Override
	public String generateToken(UserPrincipal auth) {
		String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder().setSubject(auth.getUsername()).claim("roles", authorities).claim("userId", auth.getId())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
				.signWith(key, SignatureAlgorithm.HS512).compact();

	}

	@Override
	public String generateToken(User user) {
		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder().setSubject(user.getUsername()).claim("roles", user.getRole())
				.claim("userId", user.getId())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
				.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	/**
	 * Obtiene la authentication Convertir ell token plano y debe convertirlo en un
	 * objeto de tipo ususrio para saber si es correcto
	 */

	@Override
	public Authentication getAuthentication(HttpServletRequest request) {
		Claims claims = extractClaims(request); // llamo a mi metodo
		if (claims == null) {
			return null;
		}
		// valores k me envia el cliente
		String username = claims.getSubject();
		Long userId = claims.get("userId", Long.class);
		// Obtener los roles k me envain desde el token y meterlos a una coleccion
		// separados por comas ya k son valores unicos
		Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
				.map(SecurityUtils::convertToAuthority).collect(Collectors.toSet());
		// Crear el objeto Usuario y setearle sus valores
		// Crear el objeto Usuario y setearle sus valores
		UserPrincipal userDetails = new UserPrincipal();

		userDetails.setUsername(username);
		userDetails.setAuthorities(authorities);
		userDetails.setId(userId);

		if (username == null) {
			return null;
		}
		// retorno un objeto de tipo userDetails
		return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	}

	/**
	 * Validar si el token es valido
	 */
	@Override
	public boolean isTokenValid(HttpServletRequest request) {
		Claims claims = extractClaims(request);
		// valido si el token esta vacio
		if (claims == null) {
			return false;
		}
		// validar si el token ya expiro
		if (claims.getExpiration().before(new Date())) {
			return false;
		}

		return true;
	}

	/**
	 * Metodo que extrae todos los valores o propiedades que estan dentro del token
	 * 
	 */

	private Claims extractClaims(HttpServletRequest request) {
		/*
		 * Los claims son los valores k estan al interior del token, yo yo nesesito
		 * saber k es lo k me esta enviando el cliente dentro de esos claims
		 */

		// Llamar al mi metodo que captura el token del requets del cliente le paso el
		// requets objeto

		String token = SecurityUtils.extractAuthTokenFromRequest(request);

		if (token == null) {
			return null;
		}
		// hay k desencriptarlo con la key
		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}
